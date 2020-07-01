package vn.com.pn.screen.m001User.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class UserRequest {

    private String fullName;

    private String username;

    @Email
    private String email;

    private String password;

    private String phone;
}
