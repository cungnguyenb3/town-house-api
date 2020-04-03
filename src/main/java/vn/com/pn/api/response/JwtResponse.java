package vn.com.pn.api.response;

import lombok.Data;
import vn.com.pn.service.user.UserPrinciple;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private UserPrinciple userPrinciple;

    public JwtResponse(String accessToken, UserPrinciple userPrinciple) {
        this.token = accessToken;
        this.userPrinciple = userPrinciple;
    }

    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }
}
