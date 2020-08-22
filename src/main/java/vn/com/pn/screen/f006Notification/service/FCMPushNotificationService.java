package vn.com.pn.screen.f006Notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.com.pn.screen.f006Notification.dto.FCMRequestDto;

@Service
public class FCMPushNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FCMPushNotificationService.class);

    private String androidFcmKey = "AAAADkvfv60:APA91bH7ov0bkxqVfpyhdjWxY_eFVp2nF04UsofcotOUW_jyy5fzKQpBq7sbFOpqg2nkn4G4AV1qblyDwap-evePpLR_SEvK5nKQ07LNbRNtJS3Dd4NPwbU9zT-i-WI_6HUlC20Dw49s";
    private String androidFcmUrl = "https://fcm.googleapis.com/fcm/send";

    private ObjectMapper mapper;

    public void pushNotification(String deviceToken, String content) throws JsonProcessingException {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + androidFcmKey);
        httpHeaders.set("Content-Type", "application/json");

        mapper = new ObjectMapper();

        LOGGER.info("Sending push notification to device_id {}", deviceToken);

        RestTemplate restTemplate = new RestTemplate();
        FCMRequestDto dto = new FCMRequestDto();
        dto.setTo(deviceToken);
        dto.getData().setBody(content);

        HttpEntity<String> httpEntity = new HttpEntity<String>(mapper.writeValueAsString(dto), httpHeaders);
        restTemplate.postForObject(androidFcmUrl, httpEntity, String.class);
    }
}
