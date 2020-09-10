package vn.com.pn.screen.f006Notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.com.pn.screen.f006Notification.dto.FCMDataRequestDto;
import vn.com.pn.screen.f006Notification.dto.FCMRequestDto;

import java.util.List;

@Service
public class FCMPushNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FCMPushNotificationService.class);

    private String androidFcmKey = "AAAATzxDdxo:APA91bHtUCvAgqiLcOt_69M5VRssBAc_qYFsN0lFLThj0bgLEYY7zXW4pjp0nxKBkMBFJGw69Pnk8lbTx7GMdKUte0GNDpYRh0Wr29_Im0lEz475N-ggJuQfIB1INKwKWU5YS7SbFzDB";
    private String androidFcmUrl = "https://fcm.googleapis.com/fcm/send";

    private ObjectMapper mapper;

    public String pushNotification(String deviceToken, String content) throws JsonProcessingException {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + androidFcmKey);
        httpHeaders.set("Content-Type", "application/json");

        mapper = new ObjectMapper();

        LOGGER.info("Sending push notification to device_id {}", deviceToken);

        RestTemplate restTemplate = new RestTemplate();
        FCMRequestDto dto = new FCMRequestDto();
        dto.setTo(deviceToken);
        dto.getData().setBody(content);
        dto.getNotification().setBody(content);

        HttpEntity<String> httpEntity = new HttpEntity<String>(mapper.writeValueAsString(dto), httpHeaders);
        String result = restTemplate.postForObject(androidFcmUrl, httpEntity, String.class);
        return result;
    }

    public void pushNotification(List<String> deviceTokens, FCMDataRequestDto object) throws JsonProcessingException {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + androidFcmKey);
        httpHeaders.set("Content-Type", "application/json");

        mapper = new ObjectMapper();

        RestTemplate restTemplate = new RestTemplate();
        FCMRequestDto dto = new FCMRequestDto();

        dto.setData(object);

        deviceTokens.stream().forEach(item ->{
            dto.setTo(item);
            try {
                HttpEntity<String> httpEntity = new HttpEntity<String>(mapper.writeValueAsString(dto), httpHeaders);
                LOGGER.info("pushNotification httpEntity: "+httpEntity);
                String response = restTemplate.postForObject(androidFcmUrl, httpEntity, String.class);
                LOGGER.info("pushNotification response: "+response);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}
