package vn.com.pn.screen.m001User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.pn.screen.m001User.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Modifying
    @Query(value = "Delete from tokens where token_id = :token", nativeQuery = true)
    void deleteDeviceToken(@Param("token") String token);
}
