package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.pn.domain.Host;
import vn.com.pn.domain.Role;

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
