package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO extends BaseDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String password;
}
