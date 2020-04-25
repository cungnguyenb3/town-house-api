package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSendForgotPasswordRequest {
    private String email;
}
