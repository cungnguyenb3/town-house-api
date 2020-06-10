package vn.com.pn.screen.m001User.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String fullName;
    private String userName;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String national;
    private String gender;
}
