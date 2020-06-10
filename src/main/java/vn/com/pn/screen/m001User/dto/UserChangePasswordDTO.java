package vn.com.pn.screen.m001User.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordDTO {
    private String id;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
