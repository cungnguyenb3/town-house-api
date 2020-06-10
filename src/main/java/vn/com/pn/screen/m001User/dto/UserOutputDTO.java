package vn.com.pn.screen.m001User.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.pn.screen.m002Host.entity.Host;
import vn.com.pn.screen.m003Role.entity.Role;

import java.util.Set;

@Getter
@Setter
public class UserOutputDTO {
    private int id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String national;
    private String gender;
    private Set<Role> roles;
    private Set<Host> hosts;
    private boolean enable;
}
