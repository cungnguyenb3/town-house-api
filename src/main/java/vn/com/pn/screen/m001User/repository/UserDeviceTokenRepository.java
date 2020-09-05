package vn.com.pn.screen.m001User.repository;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.pn.screen.m001User.entity.UserDeviceToken;

import java.util.List;

public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {
    List<UserDeviceToken> findByDeviceToken(String deviceToken);

    @Modifying
    @Query(value = "Delete from device_token where device_token = :deviceToken", nativeQuery = true)
    void deleteDeviceToken(@Param("deviceToken") String deviceToken);
}
