package vn.com.pn.screen.f006Notification;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.f006Notification.service.FCMPushNotificationService;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
public class PushNotiController {

    @Autowired
    private FCMPushNotificationService fcmService;

    @SneakyThrows
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public void test(@RequestParam String deviceId, @RequestParam String content) {
        fcmService.pushNotification(deviceId, content);
    }
}
