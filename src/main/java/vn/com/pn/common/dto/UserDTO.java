package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends BaseDTO{
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String phone;
}
