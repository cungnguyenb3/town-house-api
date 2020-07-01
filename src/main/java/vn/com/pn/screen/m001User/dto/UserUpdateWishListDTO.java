package vn.com.pn.screen.m001User.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserUpdateWishListDTO {
    private String id;
    private Set<String> hostIds;
}
