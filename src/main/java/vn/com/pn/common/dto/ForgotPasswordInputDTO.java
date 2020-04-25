package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordInputDTO {
    private String code;
    private String newPassword;
    private String confirmNewPassword;
}