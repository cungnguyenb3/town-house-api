package vn.com.pn.screen.m013Momo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.exception.MoMoException;
import vn.com.pn.screen.f002Booking.entity.Booking;
import vn.com.pn.screen.f002Booking.repository.BookingRepository;
import vn.com.pn.screen.m013Momo.common.MomoConstants;
import vn.com.pn.screen.m013Momo.common.Parameter;
import vn.com.pn.screen.m013Momo.dto.MomoIPNResponseDTO;
import vn.com.pn.screen.m013Momo.dto.RequestPaymentDTO;
import vn.com.pn.screen.m013Momo.entity.MomoBasicRequest;
import vn.com.pn.screen.m013Momo.entity.MomoFirstResponse;
import vn.com.pn.screen.m013Momo.repository.MomoBasicRequestRepository;
import vn.com.pn.screen.m013Momo.repository.MomoFirstResponseRepository;
import vn.com.pn.screen.m013Momo.request.MomoBasicInfoRequest;
import vn.com.pn.screen.m013Momo.request.MomoIpnPaymentRequest;
import vn.com.pn.screen.m013Momo.utils.Encoder;
import vn.com.pn.screen.m013Momo.utils.LogUtils;
import vn.com.pn.utils.MapperUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MomoService {
    private static Log logger = LogFactory.getLog(MomoService.class);

    @Autowired
    private MomoBasicRequestRepository momoBasicRequestRepository;

    @Autowired
    private MomoFirstResponseRepository momoFirstResponseRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public ResponseEntity<?> sendRequestPayment(MomoBasicInfoRequest request) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();

        RestTemplate restTemplate = new RestTemplate();

        RequestPaymentDTO dto = new RequestPaymentDTO();
        dto.setPartnerCode(MomoConstants.PARTNER_CODE);
        dto.setPartnerRefId(request.getBookingCode());
        dto.setCustomerNumber(request.getPhoneNumber());
        dto.setAppData(request.getData());
        dto.setDescription(request.getDescription());
        dto.setHash(hashRsaJsonString(request.getBookingCode(), request.getAmount(),
                null, null, null));
        dto.setVersion(MomoConstants.VERSION);
        dto.setPayType(3);

        logger.info(dto.toString());
        momoBasicRequestRepository.save(MapperUtil.mapper(request, MomoBasicRequest.class));

        HttpEntity<String> httpEntity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        MomoFirstResponse result = restTemplate.postForObject(MomoConstants.MOMO_SANDBOX_DOMAIN + "/pay/app", httpEntity, MomoFirstResponse.class);
        logger.info(CommonFunction.convertToJSONString(result));
        momoFirstResponseRepository.save(result);
        return ResponseEntity.ok("Request successfully");
    }

    public BaseOutput getAllMomoBasicRequest() {
        return CommonFunction.successOutput(momoBasicRequestRepository.findAll());
    }

    private String hashRsaJsonString(String partnerRefId, String amount,
                     String partnerTransId, String storeId, String storeName) {
        try {
            Map<String, Object> rawData = new HashMap<>();
            rawData.put(Parameter.PARTNER_CODE, MomoConstants.PARTNER_CODE);
            rawData.put(Parameter.PARTNER_REF_ID, partnerRefId);
            rawData.put(Parameter.AMOUNT, Long.parseLong(amount));
            rawData.put(Parameter.PARTNER_NAME, MomoConstants.PARTNER_NAME);
            if (partnerTransId != null && !partnerTransId.isEmpty()) {
                rawData.put(Parameter.PARTNER_TRANS_ID, partnerTransId);
            }
            if (storeId != null && !storeId.isEmpty()) {
                rawData.put(Parameter.STORE_ID, storeId);
            }
            if (storeName != null && !storeName.isEmpty()) {
                rawData.put(Parameter.STORE_NAME, storeName);
            }

            Gson gson = new Gson();

            String jsonStr = gson.toJson(rawData);
            byte[] testByte =jsonStr.getBytes(StandardCharsets.UTF_8);
            String hashRSA = Encoder.encryptRSA(testByte, MomoConstants.PUBLIC_KEY);

//            LogUtils.debug("[TransactionRefundRequest] rawData: " + rawData + ", [Signature] -> " + hashRSA);

            return hashRSA;
        } catch (Exception e) {
            LogUtils.error("[TransactionRefundRequest] "+ e);
        }
        return null;
    }

    public MomoIPNResponseDTO validateIPN(MomoIpnPaymentRequest request) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, MoMoException {
        logger.info("MomoService.validateIPN");
        logger.info(CommonFunction.convertToJSONString(request));
        String rawData =
                      Parameter.ACCESS_KEY + "=" + request.getAccessKey() +
                "&" + Parameter.AMOUNT + "=" + request.getAmount() +
                "&" + Parameter.MESSAGE + "=" + request.getMessage() +
                "&" + Parameter.MOMO_TRANS_ID + "=" + request.getMomoTransId() +
                "&" + Parameter.PARTNER_CODE + "=" + request.getPartnerCode() +
                "&" + Parameter.PARTNER_REF_ID + "=" + request.getPartnerRefId() +
                "&" + Parameter.PARTNER_TRANS_ID + "=" + request.getPartnerTransId() +
                "&" + Parameter.RESPONSE_TIME + "=" + request.getResponseTime() +
                "&" + Parameter.STATUS + "=" + request.getStatus() +
                "&" + Parameter.STORE_ID + "=" + request.getStoreId() +
                "&" + Parameter.TRANS_TYPE + "=" + request.getTransType();
        logger.info("rawDataIPN: " + rawData);

        String signature = Encoder.signHmacSHA256(rawData, MomoConstants.SECRET_KEY);
        logger.info("signature: " + signature);

        if (signature.equals(request.getSignature())) {
            logger.info("Successfull");
            MomoIPNResponseDTO momoIPNResponseDTO = new MomoIPNResponseDTO();

            Booking booking = bookingRepository.findByBookingCode(request.getPartnerRefId()).orElse(null);
            if (booking == null) {
                momoIPNResponseDTO.setStatus(400);
                return momoIPNResponseDTO;
            }
            booking.setPaid(true);
            bookingRepository.save(booking);

            momoIPNResponseDTO.setStatus(request.getStatus());
            momoIPNResponseDTO.setMessage(request.getMessage());
            momoIPNResponseDTO.setAmount(request.getAmount());
            momoIPNResponseDTO.setPartnerRefId(request.getPartnerRefId());
            momoIPNResponseDTO.setMomoTransId(request.getMomoTransId());

            String rawDateResponse = Parameter.AMOUNT + "=" + momoIPNResponseDTO.getAmount() +
                    "&" + Parameter.MESSAGE + "=" + momoIPNResponseDTO.getMessage() +
                    "&" + Parameter.MOMO_TRANS_ID + "=" + momoIPNResponseDTO.getMomoTransId() +
                    "&" + Parameter.PARTNER_REF_ID + "=" + momoIPNResponseDTO.getPartnerRefId() +
                    "&" + Parameter.STATUS + "=" + momoIPNResponseDTO.getStatus();
            String signatureResponse = Encoder.signHmacSHA256(rawDateResponse, MomoConstants.SECRET_KEY);
            momoIPNResponseDTO.setSignature(signatureResponse);
            return momoIPNResponseDTO;
        } else {
            throw new MoMoException("Wrong signature from MoMo side - please contact with us");
        }
    }
}
