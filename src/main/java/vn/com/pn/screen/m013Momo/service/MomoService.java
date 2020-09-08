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
import vn.com.pn.screen.m013Momo.common.MomoConstants;
import vn.com.pn.screen.m013Momo.common.Parameter;
import vn.com.pn.screen.m013Momo.controller.MomoController;
import vn.com.pn.screen.m013Momo.dto.RequestPaymentDTO;
import vn.com.pn.screen.m013Momo.entity.MomoBasicRequest;
import vn.com.pn.screen.m013Momo.repository.MomoBasicRequestRepository;
import vn.com.pn.screen.m013Momo.request.MomoBasicInfoRequest;
import vn.com.pn.screen.m013Momo.utils.Encoder;
import vn.com.pn.screen.m013Momo.utils.LogUtils;
import vn.com.pn.utils.MapperUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class MomoService {
    private static Log logger = LogFactory.getLog(MomoService.class);

    @Autowired
    private MomoBasicRequestRepository momoBasicRequestRepository;

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

        Gson gson = new Gson();
        String jsonStr = gson.toJson(dto);

        logger.info(jsonStr);
        momoBasicRequestRepository.save(MapperUtil.mapper(request, MomoBasicRequest.class));

//        HttpEntity<String> httpEntity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        return ResponseEntity.ok(dto);
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
}
