package vn.com.pn.screen.f006Notification.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.screen.f006Notification.dto.FireBaseRequest;
import vn.com.pn.screen.f006Notification.service.FCMPushNotificationService;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
public class PushNotiController {

    @Autowired
    private FCMPushNotificationService firebaseService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public void test(@RequestParam String deviceToken, @RequestParam String content) throws JsonProcessingException {
        firebaseService.pushNotification(deviceToken, content);
    }
}
