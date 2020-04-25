package vn.com.pn.service.forgotpasswordcode;

import vn.com.pn.common.dto.ForgotPasswordInputDTO;
import vn.com.pn.domain.ForgotPasswordCode;

import java.util.List;

public interface ForgotPasswordCodeService {
    List<ForgotPasswordCode> getAll();

}
