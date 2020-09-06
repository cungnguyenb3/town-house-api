package vn.com.pn.common.singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.com.pn.screen.m006HostCategory.repository.HostCategoryRepository;
import vn.com.pn.screen.m004HostCity.repository.HostCityRepository;
import vn.com.pn.screen.m011HostRule.entity.Rule;
import vn.com.pn.screen.m012Language.entity.Language;
import vn.com.pn.screen.m012Language.repository.LanguageRepository;
import vn.com.pn.screen.m003Role.repository.RoleRepository;
import vn.com.pn.screen.m011HostRule.repository.RuleRepository;
import vn.com.pn.screen.m001User.repository.UserRepository;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m003Role.entity.Role;
import vn.com.pn.screen.m003Role.entity.RoleName;
import vn.com.pn.screen.m004HostCity.entity.HostCity;
import vn.com.pn.screen.m006HostCategory.entity.HostCategory;

import java.util.HashSet;
import java.util.List;

@Component
public class DemoInsertData {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HostCategoryRepository hostCategoryRepository;

    @Autowired
    private HostCityRepository hostCityRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private RuleRepository ruleRepository;

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

        if (userSuperAdmin == null) {
            User user = new User();
            user.setFullName("Super Admin");
            user.setUsername("townhousersuperadmin");
            user.setPassword(encoder.encode("townhouseapi1999-G"));
            user.setEmail("townhouseapi1999@gmail.com");
            user.setPhone("0989710452");
            user.setEnable(true);
            user.setStatus(true);
            Role superAdmin = roleRepository.findByName(RoleName.ROLE_SUPER_ADMIN).orElse(null);
            user.setRoles(new HashSet<Role>() {{
                add(superAdmin);
            }});
            userRepository.save(user);
        }

        if (hostCategoryRepository.checkHostCategoryIsEmpty() == null) {
            HostCategory hostCategoryDemo = new HostCategory();
            hostCategoryDemo.setName("Homestay");
            hostCategoryDemo.setDescription("Homestay là loại hình du lịch mà khách du lịch sẽ nghỉ, ngủ tại nhà người " +
                    "dân địa phương, nơi mà họ đặt chân đến nhằm giúp du khách khám phá, trải nghiệm và tìm hiểu phong " +
                    "tục tập quán, đời sống văn hóa của từng vùng miền tại địa phương đó.");
            hostCategoryRepository.save(hostCategoryDemo);
        }

        if (hostCityRepository.checkHostCityIsEmpty() == null) {
            HostCity hostCityDemo = new HostCity();
            hostCityDemo.setName("Hà Nội");
            hostCityRepository.save(hostCityDemo);
        }

        if (languageRepository.checkLanguageIsEmpty() == null) {
            Language languageDemo = new Language();
            languageDemo.setName("Tiếng Việt");
            languageRepository.save(languageDemo);
        }

        if (ruleRepository.checkRuleIsEmpty() == null) {
            Rule ruleDemo = new Rule();
            ruleDemo.setName("Không được hút thuốc.");
            ruleRepository.save(ruleDemo);
        }
    }
}
