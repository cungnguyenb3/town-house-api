package vn.com.pn.common.singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.com.pn.domain.Role;
import vn.com.pn.domain.RoleName;
import vn.com.pn.domain.User;
import vn.com.pn.repository.role.RoleRepository;
import vn.com.pn.repository.user.UserRepository;

import java.util.HashSet;

@Component
public class DemoInsertRole {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        Role roleSuperAdmin = roleRepository.findByName(RoleName.ROLE_SUPER_ADMIN).orElse(null);
        Role roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN).orElse(null);
        Role roleAgent = roleRepository.findByName(RoleName.ROLE_HOST_AGENT).orElse(null);
        Role roleUser = roleRepository.findByName(RoleName.ROLE_USER).orElse(null);
        User userSuperAdmin = userRepository.findByUsername("townhousersuperadmin").orElse(null);

        if (roleSuperAdmin == null) {
            Role role = new Role();
            role.setName(RoleName.ROLE_SUPER_ADMIN);
            roleRepository.save(role);
        }

        if (roleAdmin == null) {
            Role role = new Role();
            role.setName(RoleName.ROLE_ADMIN);
            roleRepository.save(role);
        }

        if (roleAgent == null) {
            Role role = new Role();
            role.setName(RoleName.ROLE_HOST_AGENT);
            roleRepository.save(role);
        }

        if (roleUser == null) {
            Role role = new Role();
            role.setName(RoleName.ROLE_USER);
            roleRepository.save(role);
        }

        if (userSuperAdmin == null ) {
            User user = new User();
            user.setFullName("Super Admin");
            user.setUsername("townhousersuperadmin");
            user.setPassword(encoder.encode("townhouseapi1999-G"));
            user.setEmail("townhouseapi1999@gmail.com");
            user.setEnable(true);
            Role superAdmin = roleRepository.findByName(RoleName.ROLE_SUPER_ADMIN).orElse(null);
            user.setRoles(new HashSet<Role>(){{add(superAdmin);}});
            userRepository.save(user);
        }
    }
}
