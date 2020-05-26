package vn.com.pn.service.user;

import io.jsonwebtoken.Clock;
import io.jsonwebtoken.impl.DefaultClock;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.*;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.*;
import vn.com.pn.exception.ResourceInternalServerError;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.repository.forgotpasswordcode.ForgotPasswordCodeRepository;
import vn.com.pn.repository.host.HostRepository;
import vn.com.pn.repository.role.RoleRepository;
import vn.com.pn.repository.user.UserRepository;
import vn.com.pn.security.JwtProvider;
import vn.com.pn.service.mail.MailService;
import vn.com.pn.utils.MapperUtil;


import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static Log logger = LogFactory.getLog(UserServiceImpl.class);

    private Clock clock = DefaultClock.INSTANCE;

    private long forgotPasswordCodeExpiration = 3600;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ForgotPasswordCodeRepository forgotPasswordCodeRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("UserServiceImpl.getAll");
        List<Object> listUser = new ArrayList<Object>(userRepository.findAll());
        List<UserOutputDTO> userOutputDTOs = new ArrayList<>();
        for (Object user: listUser) {
            userOutputDTOs.add(MapperUtil.mapper(user, UserOutputDTO.class));
        }
        return CommonFunction.successOutput(userOutputDTOs);
    }

    @Override
    public BaseOutput getId(String userId) {
        logger.info("UserServiceImpl.getId");
            if (StringUtils.isNumeric(userId)) {
                User user = userRepository.findById(Long.parseLong(userId)).orElse(null);
                if (user != null){
                    UserOutputDTO userOutputDTO = MapperUtil.mapper(user, UserOutputDTO.class);
                    return CommonFunction.successOutput(userOutputDTO);
                }
                throw new ResourceNotFoundException("User", "id",userId);
            }
            throw new ResourceInternalServerError("For input string: %s" + userId);
    }

    @Override
    public BaseOutput delete(String userId) {
        logger.info("UserServiceImpl.delete");
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(()
                -> new ResourceNotFoundException("User","id", userId));
        userRepository.delete(user);
        Object object = ResponseEntity.ok().build();
        return CommonFunction.successOutput(object);
    }

    @Override
    public BaseOutput insert (UserDTO userDTO, boolean isRegisterAdmin) {
        logger.info("UserServiceImpl.insert");
        try {
            if(userRepository.existsByUsername(userDTO.getUsername())) {
                return CommonFunction.errorLogic(400,"Fail -> Username is already taken!");
            }
            if(userRepository.existsByEmail(userDTO.getEmail())) {
                return CommonFunction.errorLogic(400,"Fail -> Email is already in use!");
            }
            User user = userRepository.save(getInsertUserInfo(userDTO, isRegisterAdmin));
            sendEmailConfirm(userDTO);
            return CommonFunction.successOutput(user);
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @Override
    public BaseOutput update(UserUpdateDTO userUpdateDTO) {
        logger.info("UserServiceImpl.update");
        try {
            User user = userRepository.findById(Long.parseLong(userUpdateDTO.getId())).orElseThrow(()
                    -> new ResourceNotFoundException("user", "id", userUpdateDTO.getId()));
            if (userUpdateDTO.getFullName() != null && userUpdateDTO.getFullName() != ""){
                user.setFullName(userUpdateDTO.getFullName());
            }
            if (userUpdateDTO.getUserName() != null && userUpdateDTO.getUserName() != ""){
                user.setUsername(userUpdateDTO.getUserName());
            }
            if (userUpdateDTO.getEmail() != null && userUpdateDTO.getEmail() != ""){
                user.setEmail(userUpdateDTO.getEmail());
            }
            if (userUpdateDTO.getPhone() != null && userUpdateDTO.getPhone() != ""){
                user.setPhone(userUpdateDTO.getPhone());
            }
            if (userUpdateDTO.getDateOfBirth() != null && userUpdateDTO.getDateOfBirth() != ""){
                user.setDateOfBirth(CommonFunction.convertStringToDateObject(userUpdateDTO.getDateOfBirth()));
            }
            if (userUpdateDTO.getNational() != null && userUpdateDTO.getNational() != ""){
                user.setNational(userUpdateDTO.getNational());
            }
            if (userUpdateDTO.getGender() != null && userUpdateDTO.getGender() != ""){
                user.setGender(userUpdateDTO.getGender());
            }
            return CommonFunction.successOutput(userRepository.save(user));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @Override
    public BaseOutput changePassword(UserChangePasswordDTO userChangePasswordDTO) {
        logger.info("UserServiceImpl.changePassword");
        try {
           User user = userRepository.findById(Long.parseLong(userChangePasswordDTO.getId())).orElseThrow(()
                    -> new  ResourceNotFoundException("User", "id", userChangePasswordDTO.getId()));
            if (userChangePasswordDTO.getCurrentPassword() != null && userChangePasswordDTO.getCurrentPassword() != ""
                && userChangePasswordDTO.getNewPassword() != null && userChangePasswordDTO.getNewPassword() != ""
                    && userChangePasswordDTO.getConfirmNewPassword() != null && userChangePasswordDTO.getConfirmNewPassword() != ""
            ) {
                if (encoder.matches(userChangePasswordDTO.getCurrentPassword(),user.getPassword())) {
                    if (userChangePasswordDTO.getNewPassword().equalsIgnoreCase(userChangePasswordDTO.getConfirmNewPassword())){
                        if (encoder.matches(userChangePasswordDTO.getNewPassword(),user.getPassword())) {
                            return CommonFunction.errorLogic(400, "New passwords are not allowed to match with the current password ");
                        }
                        user.setPassword(encoder.encode(userChangePasswordDTO.getNewPassword()));
                        sendEmailRemindForChangedPassword(user);
                        return CommonFunction.successOutput(userRepository.save(user));
                    } else {
                        logger.error("Two new passwords do not match");
                        return CommonFunction.errorLogic(400,"Two new passwords do not match");
                    }
                } else {
                    logger.error("Current password don't correct");
                    return CommonFunction.errorLogic(400,"Current password don't correct");
                }
            } else {
                logger.error("Please full all field");
                return CommonFunction.errorLogic(400,"Please full all field");
            }
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @Override
    public BaseOutput updateWishListHost(UserUpdateWishListDTO userUpdateWishListDTO) {
        logger.info("UserServiceImpl.updateWishListHost");
        try {
            User user = userRepository.findById(Long.parseLong(userUpdateWishListDTO.getId())).orElseThrow(()
                    -> new  ResourceNotFoundException("User", "id", userUpdateWishListDTO.getId()));
            Set<String> strHostIds = userUpdateWishListDTO.getHostIds();
            Set<Host> hosts = new HashSet<>();

            //Two way to loop the Set
            //Way 1
//            strHostIds.forEach(hostId -> {
//                if (hostId != null && hostId != ""){
//                    Host host = hostRepository.findById(Integer.parseInt(hostId)).orElseThrow(()
//                            -> new  ResourceNotFoundException("Host", "id", hostId));
//                    hosts.add(host);
//                }
//            });

            //Way 2
            for (String hostId : strHostIds) {
                if (hostId != null && hostId != ""){
                    Host host = hostRepository.findById(Long.parseLong(hostId)).orElseThrow(()
                            -> new  ResourceNotFoundException("Host", "id", hostId));
                    hosts.add(host);
                }
            }
            user.setHosts(hosts);
            return CommonFunction.successOutput(CommonFunction.successOutput(userRepository.save(user)));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    private User getInsertUserInfo (UserDTO userDTO, boolean isRegisterAdmin){
        logger.info("UserServiceImpl.getInsertUserInfo");
        User user = new User();
        if (userDTO.getFullName() != null && userDTO.getFullName() != "") {
            user.setFullName(userDTO.getFullName());
        }
        if (userDTO.getUsername() != null && userDTO.getUsername() != "") {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getPassword() != null && userDTO.getPassword() != "") {
            user.setPassword(encoder.encode(userDTO.getPassword()));
        }
        if (userDTO.getEmail() != null && userDTO.getEmail() != "") {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPhone() != null && userDTO.getPhone() == "") {
            user.setPhone(userDTO.getPhone());
        }
        user.setEnable(false);
        Set<Role> roles = new HashSet<>();
        if (isRegisterAdmin) {
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
            roles.add(adminRole);
        } else {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
            Role agentRole = roleRepository.findByName(RoleName.ROLE_HOST_AGENT)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Host Agent Role not find."));
            roles.add(userRole);
            roles.add(agentRole);
        }
        user.setRoles(roles);
        return user;
    }

    private String generateTokenForActiveUser (String username, String password) {
        logger.info("UserServiceImpl.generateTokenForActiveUser");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateJwtToken(authentication);
    }

    private void sendEmailConfirm(UserDTO userDTO){
        logger.info("UserServiceImpl.sendEmailConfirm");
        try {
            String token = generateTokenForActiveUser(userDTO.getUsername(), userDTO.getPassword());
            String emailSubject = "Xác thực địa chỉ email";
            String greeting = "Xin chào " + userDTO.getFullName() + ", \n\n";
            String thanks = "Cảm ơn bạn đã đăng ký sử dụng dịch vụ tại Town House. " +
                    "Vui lòng click vào link bên dưới để hoàn tất đăng ký tài khoản. \n\n" ;
            String localhostUrl = "http://localhost:8080/api/users/activation?token="+token;
            String sign = "Trân trọng, \nTown House team";
            //For deploy on heroku
            String herokuUrl = "https://town-house-api-seven-team.herokuapp.com/api/users/activation?token="+token;

            StringBuilder emailContent = new StringBuilder();
            emailContent.append(greeting).append(thanks).append(localhostUrl).append("\n\n").append(sign);
            mailService.sendEmail(userDTO.getEmail(), emailSubject, emailContent);

        } catch (MailException mailException) {
            logger.error(ScreenMessageConstants.FAILURE, mailException);
        }
    }

    @Override
    public BaseOutput enableUser(UserPrinciple userPrinciple) {
        logger.info("UserServiceImpl.enableUser");
        try {
            User user = userRepository.findById(userPrinciple.getUser().getId()).orElseThrow(()
                    -> new  ResourceNotFoundException("User", "id", userPrinciple.getUser().getId()));
            user.setEnable(true);
            return CommonFunction.successOutput(userRepository.save(user));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @Override
    public BaseOutput forgotPassword(String email) {
        logger.info("UserServiceImpl.forgotPassword");
        List<User> listUser = new ArrayList<User>(userRepository.findAll());
        for (User user : listUser) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                Date createDate = clock.now();
                Date expirationDate = new Date(createDate.getTime() + forgotPasswordCodeExpiration * 300);
                ForgotPasswordCode forgotPasswordCode = new ForgotPasswordCode();
                forgotPasswordCode.setEmail(email);
                forgotPasswordCode.setCode(getForgotPasswordCode());
                forgotPasswordCode.setUsed(false);
                forgotPasswordCode.setCreateDate(createDate);
                forgotPasswordCode.setExpirationDate(expirationDate);
                forgotPasswordCodeRepository.save(forgotPasswordCode);
                String emailSubject = "Yêu cầu đặt lại mật khẩu";
                String greeting = "Xin chào " + user.getFullName();
                String code = "Mã đặt lại mật khẩu: ";
                String thanks = "Cảm ơn bạn dã sử dụng dịch vụ của Town Hose";
                String sign = "Trân trọng, \nTown house team";
                StringBuilder emailContent = new StringBuilder();
                emailContent.append(greeting).append(",\n\n")
                        .append(code).append(forgotPasswordCode.getCode())
                        .append("\n\n").append(thanks)
                        .append("\n\n").append(sign);
                mailService.sendEmail(user.getEmail(), emailSubject, emailContent);
                return CommonFunction.successOutput(email);
            }
        }
        return CommonFunction.failureOutput();
    }

    public String getForgotPasswordCode() {
        logger.info("UserServiceImpl.getForgotPasswordCode");
        int randomNum = ThreadLocalRandom.current().nextInt(100000, 999998 + 1);
        return String.valueOf(randomNum);
    }

    @Override
    public BaseOutput handleForgotPassword(ForgotPasswordInputDTO forgotPasswordInputDTO) {
        logger.info("UserServiceImpl.handleForgotPassword");
        List<ForgotPasswordCode> forgotPasswordCodes = forgotPasswordCodeRepository.findAll();
        for (ForgotPasswordCode forgotPasswordCode: forgotPasswordCodes) {
            if (forgotPasswordCode.getCode().equalsIgnoreCase(forgotPasswordInputDTO.getCode())) {
                User user = userRepository.findByEmail(forgotPasswordCode.getEmail()).orElseThrow(()
                        -> new  ResourceNotFoundException("User", "email", forgotPasswordCode.getEmail()));
                if (forgotPasswordCode.getExpirationDate().before(clock.now())){
                    return CommonFunction.errorLogic(400, "Code has been expiration");
                }
                if (!forgotPasswordCode.isUsed()) {
                    if (encoder.matches(forgotPasswordInputDTO.getNewPassword(), user.getPassword())){
                        return CommonFunction.errorLogic(400, "New passwords are not allowed to match with the current password");
                    }
                    if (forgotPasswordInputDTO.getNewPassword().equalsIgnoreCase(forgotPasswordInputDTO.getConfirmNewPassword())) {
                        forgotPasswordCode.setUsed(true);
                        forgotPasswordCodeRepository.save(forgotPasswordCode);
                        changePassword(user, forgotPasswordInputDTO.getNewPassword());
                        return CommonFunction.successOutput(userRepository.save(user));
                    }
                    return CommonFunction.errorLogic(400, "Two password's input do not match");
                }
                return CommonFunction.errorLogic(400, "Code has been used");
            }
        }
        return CommonFunction.errorLogic(404, "Không tìm thấy user");
    }

    private User changePassword(User user, String newPassword){
        user.setPassword(encoder.encode(newPassword));
        return user;
    }

    private User sendEmailRemindForChangedPassword(User user) {
        String emailSubject = "Mật khẩu của bạn đã được thay đổi";
        String greeting = "Xin chào " + user.getFullName();
        String remind = "Chúng tôi muốn cho bạn biết rằng, mật khẩu Town House của bạn đã được thay đổi.";
        String ifUserNotDid = "Nếu bạn không làm điều này, vui lòng thực hiện chức năng quên mật khẩu.";
        String ifHasProblem = "Nếu bạn đã gặp bất kỳ vấn đề gì, xin vui lòng liên hệ với bộ phận hỗ trợ của chúng tôi.";
        String pleaseDontReply = "Bạn không nên trả lời lại email này. Chúng tôi sẽ không bao giờ yêu cầu mật khẩu của " +
                "bạn và chúng tôi không khuyến khích bạn chia sẻ nó với bất kỳ ai.";
        String sign = "Trân trọng, \nTown house team";
        StringBuilder emailContent = new StringBuilder();
        emailContent.append(greeting)
                .append("\n\n").append(remind)
                .append("\n\n").append(ifUserNotDid)
                .append("\n\n").append(ifHasProblem)
                .append("\n\n").append(pleaseDontReply)
                .append("\n\n").append(sign);
        mailService.sendEmail(user.getEmail(), emailSubject, emailContent);
        return user;
    }
}
