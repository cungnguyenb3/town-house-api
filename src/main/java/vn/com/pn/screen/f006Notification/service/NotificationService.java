package vn.com.pn.screen.f006Notification.service;

import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.entity.User;

public interface NotificationService {
    BaseOutput getNotificationByUser(Long userId);
    BaseOutput setNotificationIsRead(Long notificationId);
}
