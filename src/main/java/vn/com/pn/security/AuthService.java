package vn.com.pn.security;

import vn.com.pn.screen.m001User.entity.User;

public interface AuthService {
    User getLoggedUser();
    boolean isLogged();
}
