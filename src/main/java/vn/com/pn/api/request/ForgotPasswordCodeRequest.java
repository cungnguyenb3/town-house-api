package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordCodeRequest {
    private String code;
    private String newPassword;
    private String confirmNewPassword;
}
