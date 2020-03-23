package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordRequest extends BaseRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
