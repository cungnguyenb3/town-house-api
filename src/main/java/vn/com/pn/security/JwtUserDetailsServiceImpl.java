package vn.com.pn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.screen.m001User.repository.UserRepository;
import vn.com.pn.screen.m001User.service.UserPrinciple;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService, AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with -> username: " + username)
                );
        return UserPrinciple.build(user);
    }

    public UserDetails loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with -> email: " + email)
                );
        return UserPrinciple.build(user);
    }

    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(()
                -> new ResourceNotFoundException("User", "id", userId));
        return UserPrinciple.build(user);
    }

    @Override
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object o = authentication.getPrincipal();
        if (o == null) {
            return null;
        }
        if (!(o instanceof UserPrinciple)) {
            return null;
        }
        return ((UserPrinciple) o).getUser();
    }

    @Override
    public boolean isLogged() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
                && !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
