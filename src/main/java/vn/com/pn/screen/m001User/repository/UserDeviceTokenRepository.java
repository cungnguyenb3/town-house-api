package vn.com.pn.screen.m001User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.screen.m001User.entity.UserDeviceToken;

public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {
}
