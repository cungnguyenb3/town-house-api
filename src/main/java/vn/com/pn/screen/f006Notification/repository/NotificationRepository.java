package vn.com.pn.screen.f006Notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.screen.f006Notification.entity.Notification;
import vn.com.pn.screen.m001User.entity.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
}
