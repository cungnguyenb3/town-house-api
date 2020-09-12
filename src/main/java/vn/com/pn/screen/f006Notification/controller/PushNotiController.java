package vn.com.pn.screen.f006Notification.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.f006Notification.dto.FireBaseRequest;
import vn.com.pn.screen.f006Notification.service.FCMPushNotificationService;
import vn.com.pn.screen.f006Notification.service.NotificationService;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.security.AuthService;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
public class PushNotiController {
    private static Log logger = LogFactory.getLog(PushNotiController.class);

    @Autowired
    private FCMPushNotificationService firebaseService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test(@RequestParam String deviceToken, @RequestParam String content) throws JsonProcessingException {
        return firebaseService.pushNotification(deviceToken, null,  content, null);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Get lists hosts specific user", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.NOTIFICATION_USER, method = RequestMethod.GET)
    public BaseOutput getNotificationByUser() {
        logger.info("========== UserController.getListHostByUser START ==========");
        User user = authService.getLoggedUser();
        BaseOutput response = notificationService.getNotificationByUser(user.getId());
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Get lists hosts specific user", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.NOTIFICATION_USER_ID, method = RequestMethod.PUT)
    public BaseOutput getNotificationByUser(@PathVariable Long id) {
        logger.info("========== UserController.getListHostByUser START ==========");
        BaseOutput response = notificationService.setNotificationIsRead(id);
        return response;
    }
}
