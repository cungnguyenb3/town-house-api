package vn.com.pn.screen.f006Notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.screen.f006Notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
