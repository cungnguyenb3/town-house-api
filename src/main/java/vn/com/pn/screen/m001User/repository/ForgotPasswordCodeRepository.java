package vn.com.pn.screen.m001User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.screen.m001User.entity.ForgotPasswordCode;

public interface ForgotPasswordCodeRepository extends JpaRepository <ForgotPasswordCode, Long> {
}
