package vn.com.pn.screen.f006Notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.f006Notification.entity.Notification;
import vn.com.pn.screen.f006Notification.repository.NotificationRepository;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m001User.repository.UserRepository;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BaseOutput getNotificationByUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            List<Notification> notifications = notificationRepository.findByUser(user);
            return CommonFunction.successOutput(notifications);
        }
        return null;
    }
}
