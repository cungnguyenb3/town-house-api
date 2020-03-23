package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserUpdateHostWishListRequest {
    private Set<String> hostId;
}
