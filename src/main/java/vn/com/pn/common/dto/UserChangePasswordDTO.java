package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordDTO extends BaseDTO {
    private String id;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
