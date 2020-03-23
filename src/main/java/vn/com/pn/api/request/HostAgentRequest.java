package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostAgentRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
}
