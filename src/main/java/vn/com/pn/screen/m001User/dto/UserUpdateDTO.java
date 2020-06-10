package vn.com.pn.screen.m001User.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private String id;
    private String fullName;
    private String userName;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String national;
    private String gender;
}
