package vn.com.pn.security;

import vn.com.pn.domain.User;

public interface AuthService {
    User getLoggedUser();
    boolean isLogged();
}
