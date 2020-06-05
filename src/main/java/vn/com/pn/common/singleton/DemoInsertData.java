package vn.com.pn.common.singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.com.pn.domain.*;
import vn.com.pn.repository.currencyunit.CurrencyUnitRepository;
import vn.com.pn.repository.hostcancellationpolicy.HostCancellationPolicyRepository;
import vn.com.pn.repository.hostcategory.HostCategoryRepository;
import vn.com.pn.repository.hostcity.HostCityRepository;
import vn.com.pn.repository.hostroomtype.HostRoomTypeRepository;
import vn.com.pn.repository.language.LanguageRepository;
import vn.com.pn.repository.procedurecheckin.ProcedureCheckInRepository;
import vn.com.pn.repository.role.RoleRepository;
import vn.com.pn.repository.rule.RuleRepository;
import vn.com.pn.repository.user.UserRepository;

import java.util.HashSet;
import java.util.List;

@Component
public class DemoInsertData {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyUnitRepository currencyUnitRepository;

    @Autowired
    private HostCancellationPolicyRepository hostCancellationPolicyRepository;

    @Autowired
    private HostCategoryRepository hostCategoryRepository;

    @Autowired
    private HostCityRepository hostCityRepository;

    @Autowired
    private HostRoomTypeRepository hostRoomTypeRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ProcedureCheckInRepository procedureCheckInRepository;

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
        List<CurrencyUnit> currencyUnits = currencyUnitRepository.findAll();
        List<HostCancellationPolicy> hostCancellationPolicys = hostCancellationPolicyRepository.findAll();
        List<HostCategory> hostCategorys = hostCategoryRepository.findAll();
        List<HostCity> hostCities = hostCityRepository.findAll();
        List<HostRoomType> hostRoomTypes = hostRoomTypeRepository.findAll();
        List<Language> languages = languageRepository.findAll();
        List<ProcedureCheckIn> procedureCheckIns = procedureCheckInRepository.findAll();
        List<Rule> rules = ruleRepository.findAll();

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
            user.setPhone("0989710452");
            user.setEnable(true);
            Role superAdmin = roleRepository.findByName(RoleName.ROLE_SUPER_ADMIN).orElse(null);
            user.setRoles(new HashSet<Role>(){{add(superAdmin);}});
            userRepository.save(user);
        }

        if (currencyUnits.size() == 0) {
            CurrencyUnit currencyUnitDemo = new CurrencyUnit();
            currencyUnitDemo.setName("VNĐ");
            currencyUnitRepository.save(currencyUnitDemo);
        }

        if (hostCancellationPolicys.size() == 0) {
            HostCancellationPolicy hostCancellationPolicyDemo = new HostCancellationPolicy();
            hostCancellationPolicyDemo.setName("Linh hoạt (Flexible)");
            hostCancellationPolicyDemo.setDescription("Cơ chế: Miễn phí hủy phòng khi khách hàng hủy trước 24h so với " +
                    "giờ nhận phòng. Hoàn lại 50% giá trị đơn đặt phòng khi khách hàng hủy trong vòng 24h so với giờ nhận phòng.");
            hostCancellationPolicyRepository.save(hostCancellationPolicyDemo);
        }

        if(hostCategorys.size() == 0) {
            HostCategory hostCategoryDemo = new HostCategory();
            hostCategoryDemo.setName("Homestay");
            hostCategoryDemo.setDescription("Homestay là loại hình du lịch mà khách du lịch sẽ nghỉ, ngủ tại nhà người " +
                    "dân địa phương, nơi mà họ đặt chân đến nhằm giúp du khách khám phá, trải nghiệm và tìm hiểu phong " +
                    "tục tập quán, đời sống văn hóa của từng vùng miền tại địa phương đó.");
            hostCategoryRepository.save(hostCategoryDemo);
        }

        if (hostCities.size() == 0) {
            HostCity hostCityDemo = new HostCity();
            hostCityDemo.setName("Hà Nội");
            hostCityRepository.save(hostCityDemo);
        }

        if (hostRoomTypes.size() == 0) {
            HostRoomType hostRoomTypeDemo = new HostRoomType();
            hostRoomTypeDemo.setName("Nguyên căn");
            hostRoomTypeDemo.setDescription("Thuê nhà nguyên căn là hình thức thuê cả một căn hộ và được toàn quyền sử dụng căn nhà" +
                    " đó. Việc thuê nhà nguyên căn phù hợp với gia đình hoặc một nhóm bạn muốn có một môi trường sống" +
                    " riêng tư theo ý mình.");
            hostRoomTypeRepository.save(hostRoomTypeDemo);
        }

        if (languages.size() == 0) {
            Language languageDemo = new Language();
            languageDemo.setName("Tiếng Việt");
            languageRepository.save(languageDemo);
        }

        if (procedureCheckIns.size() == 0) {
            ProcedureCheckIn procedureCheckInDemo = new ProcedureCheckIn();
            procedureCheckInDemo.setName("Tự check in");
            procedureCheckInRepository.save(procedureCheckInDemo);
        }

        if (rules.size() == 0) {
            Rule ruleDemo = new Rule();
            ruleDemo.setName("Không được hút thuốc.");
            ruleRepository.save(ruleDemo);
        }
    }
}
