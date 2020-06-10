package vn.com.pn.screen.m001User.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordCodeRequest {
    private String code;
    private String newPassword;
    private String confirmNewPassword;
}
