package vn.com.pn.screen.f006Notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.screen.f006Notification.entity.Notification;
import vn.com.pn.screen.f006Notification.repository.NotificationRepository;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m001User.repository.UserRepository;

import java.util.Date;
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
            List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
            return CommonFunction.successOutput(notifications);
        }
        return null;
    }

    @Override
    public BaseOutput setNotificationIsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new ResourceNotFoundException("Notification", "id", notificationId)
        );
        notification.setRead(true);
        notification.setUpdatedAt(new Date());
        notificationRepository.save(notification);
        return CommonFunction.successOutput(notification);
    }
}
