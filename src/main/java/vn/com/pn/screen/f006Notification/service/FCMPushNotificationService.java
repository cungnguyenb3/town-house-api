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

    private String androidFcmKey = "AAAATzxDdxo:APA91bGhnSfHUU6cpJGXyLM3xuPmWAGKRGDdc7S64gIOBDqHYIU5yelcGIXveImLYmT6vjdct5T9Cr8_Ry1lMsuYonsR1x4Kcq3qx1XreNu_YKd2HthsDDRrGnflNEssgFOZSHG84Nuv";
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
