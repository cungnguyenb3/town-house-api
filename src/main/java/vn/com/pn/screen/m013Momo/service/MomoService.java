package vn.com.pn.screen.m013Momo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m013Momo.common.MomoConstants;
import vn.com.pn.screen.m013Momo.dto.RequestPaymentDTO;
import vn.com.pn.screen.m013Momo.entity.MomoBasicRequest;
import vn.com.pn.screen.m013Momo.repository.MomoBasicRequestRepository;
import vn.com.pn.screen.m013Momo.request.MomoBasicInfoRequest;
import vn.com.pn.utils.MapperUtil;

import javax.persistence.Lob;

@Service
public class MomoService {

    @Autowired
    private MomoBasicRequestRepository momoBasicRequestRepository;

    public void sendRequestPayment(MomoBasicInfoRequest request) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();

        RestTemplate restTemplate = new RestTemplate();

        RequestPaymentDTO dto = new RequestPaymentDTO();
        dto.setPartnerCode(MomoConstants.PARTNER_CODE);
        dto.setPartnerRefId(request.getBookingCode());
        dto.setCustomerNumber(request.getPhoneNumber());
        dto.setAppData(request.getData());

        momoBasicRequestRepository.save(MapperUtil.mapper(request, MomoBasicRequest.class));

        HttpEntity<String> httpEntity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
    }

    public BaseOutput getAllMomoBasicRequest() {
        return CommonFunction.successOutput(momoBasicRequestRepository.findAll());
    }
}
