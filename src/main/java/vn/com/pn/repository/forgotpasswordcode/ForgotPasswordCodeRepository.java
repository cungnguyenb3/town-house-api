package vn.com.pn.repository.forgotpasswordcode;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.domain.ForgotPasswordCode;

public interface ForgotPasswordCodeRepository extends JpaRepository <ForgotPasswordCode, Integer> {
}
