package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostAgentUpdateDTO {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String password;
}
