package vn.com.pn.screen.m001User.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordInputDTO {
    private String code;
    private String newPassword;
    private String confirmNewPassword;
}