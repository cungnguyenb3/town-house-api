package vn.com.pn.screen.m001User.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserUpdateHostWishListRequest {
    private Set<String> hostId;
}
