package vn.com.pn.screen.m001User.service;

import io.jsonwebtoken.Clock;
import io.jsonwebtoken.impl.DefaultClock;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailException;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.LogMessageConstants;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.exception.ResourceForbiddenException;
import vn.com.pn.exception.ResourceInternalServerError;
import vn.com.pn.exception.ResourceInvalidInputException;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.screen.f002Booking.entity.Booking;
import vn.com.pn.screen.f002Booking.repository.BookingRepository;
import vn.com.pn.screen.m001User.entity.ForgotPasswordCode;
import vn.com.pn.screen.m001User.entity.Token;
import vn.com.pn.screen.m001User.entity.UserDeviceToken;
import vn.com.pn.screen.m001User.repository.ForgotPasswordCodeRepository;
import vn.com.pn.screen.m001User.dto.*;
import vn.com.pn.screen.m001User.repository.TokenRepository;
import vn.com.pn.screen.m001User.repository.UserDeviceTokenRepository;
import vn.com.pn.screen.m002Host.repository.HostRepository;
import vn.com.pn.screen.m003Role.repository.RoleRepository;
import vn.com.pn.screen.m001User.repository.UserRepository;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m002Host.entity.Host;
import vn.com.pn.screen.m003Role.entity.Role;
import vn.com.pn.screen.m003Role.entity.RoleName;
import vn.com.pn.security.JwtProvider;
import vn.com.pn.screen.f005Gmail.service.MailService;
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

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserDeviceTokenRepository deviceTokenRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public BaseOutput getAll(Integer pageNo, Integer pageSize, String sortBy) {
        logger.info("UserServiceImpl.getAll");
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

//        Page<User> pagedResult = userRepository.getAllUser(paging);
        Page<User> pagedResult = userRepository.getAllUser(paging);
        List<User> users = pagedResult.getContent();

        if (pagedResult.hasContent()) {
            if (pagedResult.getNumberOfElements() == pageSize) {
                return CommonFunction.successOutput(pagedResult.getContent(), pagedResult.getSize());
            } else {
                return CommonFunction.successOutput(users);
            }
        } else {
            return CommonFunction.successOutput(new ArrayList<User>());
        }
    }

    @Override
    public BaseOutput getListHostByUser(Integer pageNo, Integer pageSize,
                                        String sortBy, Long userId) {
        logger.info("UserServiceImpl.getListHostByUser");
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Host> pagedResult = hostRepository.getHostByUser(userId, paging);
        List<Host> hosts = pagedResult.getContent();

        if (pagedResult.hasContent()) {
            if (pagedResult.getNumberOfElements() == pageSize) {
                return CommonFunction.successOutput(pagedResult.getContent(), pagedResult.getSize());
            } else {
                return CommonFunction.successOutput(hosts);
            }
        } else {
            return CommonFunction.successOutput(new ArrayList<User>());
        }
    }

    @Override
    public BaseOutput getId(String userId) {
        logger.info("UserServiceImpl.getId");
        if (StringUtils.isNumeric(userId)) {
            User user = userRepository.findById(Long.parseLong(userId)).orElse(null);
            if (user != null) {
                return CommonFunction.successOutput(user);
            }
            throw new ResourceNotFoundException("User", "id", userId);
        }
        throw new ResourceInternalServerError("For input string: %s" + userId);
    }

    @Override
    public BaseOutput delete(String userId, User userLogin) {
        logger.info("UserServiceImpl.delete");
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(()
                -> new ResourceNotFoundException("User", "id", userId));
        boolean flag = true;
        for (Role role : user.getRoles()) {
            if (role.getId() == 1l) {
                flag = false;
            }
            for (Role roleLogin : userLogin.getRoles()) {
                if (roleLogin.getId() == 3l || roleLogin.getId() == 4l) {
                    flag = false;
                }
                if (roleLogin.getId() == 2l && role.getId() == 2l) {
                    flag = false;
                }
            }
        }
        if (flag) {
            user.setStatus(false);
            userRepository.save(user);
            Object object = ResponseEntity.ok().build();
            return CommonFunction.successOutput(object);
        }
        throw new ResourceForbiddenException("Bạn không có quyền thực hiện chức năng này!");
    }

    @Override
    public BaseOutput insert(UserDTO userDTO, boolean isRegisterAdmin) {
        logger.info("UserServiceImpl.insert");

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new ResourceInvalidInputException("Username đã được sử dụng");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResourceInvalidInputException("Email đã được sử dụng");
        }
        User user = userRepository.save(getInsertUserInfo(userDTO, isRegisterAdmin));
        sendEmailConfirm(userDTO);
        return CommonFunction.successOutput(user);
    }

    @Override
    public BaseOutput update(User user, UserUpdateDTO userUpdateDTO) {
        logger.info("UserServiceImpl.update");
        try {
            if (userUpdateDTO.getFullName() != null && userUpdateDTO.getFullName() != "") {
                user.setFullName(userUpdateDTO.getFullName());
            }
            if (userUpdateDTO.getUserName() != null && userUpdateDTO.getUserName() != "") {
                user.setUsername(userUpdateDTO.getUserName());
            }
            if (userUpdateDTO.getEmail() != null && userUpdateDTO.getEmail() != "") {
                user.setEmail(userUpdateDTO.getEmail());
            }
            if (userUpdateDTO.getPhone() != null && userUpdateDTO.getPhone() != "") {
                user.setPhone(userUpdateDTO.getPhone());
            }
            if (userUpdateDTO.getDateOfBirth() != null && userUpdateDTO.getDateOfBirth() != "") {
                user.setDateOfBirth(CommonFunction.convertStringToDateObject(userUpdateDTO.getDateOfBirth()));
            }
            if (userUpdateDTO.getNational() != null && userUpdateDTO.getNational() != "") {
                user.setNational(userUpdateDTO.getNational());
            }
            if (userUpdateDTO.getGender() != null && userUpdateDTO.getGender() != "") {
                user.setGender(userUpdateDTO.getGender());
            }
            return CommonFunction.successOutput(userRepository.save(user));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    @Override
    public BaseOutput changePassword(UserChangePasswordDTO userChangePasswordDTO) {
        logger.info("UserServiceImpl.changePassword");
        try {
            User user = userRepository.findById(Long.parseLong(userChangePasswordDTO.getId())).orElseThrow(()
                    -> new ResourceNotFoundException("User", "id", userChangePasswordDTO.getId()));
            if (userChangePasswordDTO.getCurrentPassword() != null && userChangePasswordDTO.getCurrentPassword() != ""
                    && userChangePasswordDTO.getNewPassword() != null && userChangePasswordDTO.getNewPassword() != ""
                    && userChangePasswordDTO.getConfirmNewPassword() != null && userChangePasswordDTO.getConfirmNewPassword() != ""
            ) {
                if (encoder.matches(userChangePasswordDTO.getCurrentPassword(), user.getPassword())) {
                    if (userChangePasswordDTO.getNewPassword().equalsIgnoreCase(userChangePasswordDTO.getConfirmNewPassword())) {
                        if (encoder.matches(userChangePasswordDTO.getNewPassword(), user.getPassword())) {
                            throw new ResourceInvalidInputException("New passwords are not allowed to match with the current password ");
                        }
                        user.setPassword(encoder.encode(userChangePasswordDTO.getNewPassword()));
                        sendEmailRemindForChangedPassword(user);
                        return CommonFunction.successOutput(userRepository.save(user));
                    } else {
                        logger.error("Two new passwords do not match");
                        throw new ResourceInvalidInputException("Two new passwords do not match");
                    }
                } else {
                    logger.error("Current password don't correct");
                    throw new ResourceInvalidInputException("Current password don't correct");
                }
            } else {
                logger.error("Please full all field");
                throw new ResourceInvalidInputException("Tất cả các trường không được bỏ trống");
            }
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    @Override
    public BaseOutput updateWishListHost(UserUpdateWishListDTO userUpdateWishListDTO) {
        logger.info("UserServiceImpl.updateWishListHost");
        try {
            User user = userRepository.findById(Long.parseLong(userUpdateWishListDTO.getId())).orElseThrow(()
                    -> new ResourceNotFoundException("User", "id", userUpdateWishListDTO.getId()));
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
                if (hostId != null && hostId != "") {
                    Host host = hostRepository.findById(Long.parseLong(hostId)).orElseThrow(()
                            -> new ResourceNotFoundException("Host", "id", hostId));
                    hosts.add(host);
                }
            }
            user.setHosts(hosts);
            return CommonFunction.successOutput(CommonFunction.successOutput(userRepository.save(user)));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    private User getInsertUserInfo(UserDTO userDTO, boolean isRegisterAdmin) {
        logger.info("UserServiceImpl.getInsertUserInfo");
        User user = new User();
        if (userDTO.getFullName() == null || userDTO.getFullName() == "") {
            throw new ResourceInvalidInputException("Trường họ và tên không được để trống!");
        }
        user.setFullName(userDTO.getFullName());
        if (userDTO.getUsername() == null || userDTO.getUsername() == "") {
            throw new ResourceInvalidInputException("Trường tên đăng nhập không được để trống!");
        }
        user.setUsername(userDTO.getUsername());
        if (userDTO.getPassword() == null || userDTO.getPassword() == "") {
            throw new ResourceInvalidInputException("Trường mật khẩu không được để trống!");
        }
        user.setPassword(encoder.encode(userDTO.getPassword()));
        if (userDTO.getEmail() == null || userDTO.getEmail() == "") {
            throw new ResourceInvalidInputException("Trường email không được để trống!");
        }
        user.setEmail(userDTO.getEmail());
        if (userDTO.getPhone() == null || userDTO.getPhone() == "") {
            throw new ResourceInvalidInputException("Trường số điện thoại không được để trống!");
        }
        user.setPhone(userDTO.getPhone());

        user.setStatus(true);
        user.setEnable(false);
        Set<Role> roles = new HashSet<>();
        if (isRegisterAdmin) {
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Không thể tìm thấy role này"));
            roles.add(adminRole);
        } else {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Không thể tìm thấy role này"));
            Role agentRole = roleRepository.findByName(RoleName.ROLE_HOST_AGENT)
                    .orElseThrow(() -> new RuntimeException("Không thể tìm thấy role này"));
            roles.add(userRole);
            roles.add(agentRole);
        }
        user.setRoles(roles);
        return user;
    }

    private String generateTokenForActiveUser(String username, String password) {
        logger.info("UserServiceImpl.generateTokenForActiveUser");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateJwtToken(authentication);
    }

    private void sendEmailConfirm(UserDTO userDTO) {
        logger.info("UserServiceImpl.sendEmailConfirm");
        try {
            StringBuilder emailContent = new StringBuilder();

            String token = generateTokenForActiveUser(userDTO.getUsername(), userDTO.getPassword());
            String emailSubject = "Xác thực địa chỉ email";
            String localhostUrl = "http://localhost:8080/api/users/activation?token=" + token;
            //For deploy on heroku
            String herokuUrl = "https://town-house-api-seven-team.herokuapp.com/api/users/activation?token=" + token;


            emailContent.append("Xin chào " + userDTO.getFullName() + ", \n\n")
                    .append("Cảm ơn bạn đã đăng ký sử dụng dịch vụ tại Town House. " +
                            "Vui lòng click vào link bên dưới để hoàn tất đăng ký tài khoản. \n\n")
                    .append(herokuUrl)
                    .append("\n\n")
                    .append("Trân trọng, \nTown House team");

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
                    -> new ResourceNotFoundException("User", "id", userPrinciple.getUser().getId()));
            user.setEnable(true);
            return CommonFunction.successOutput(userRepository.save(user));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    @Override
    public BaseOutput forgotPassword(String email) {
        logger.info("UserServiceImpl.forgotPassword");
        List<User> listUser = new ArrayList<User>(userRepository.findAll());
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy user với email: " + email)
        );
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

        StringBuilder emailContent = new StringBuilder();

        emailContent.append("Xin chào " + user.getFullName())
                .append(",\n\nMã đặt lại mật khẩu: ")
                .append(forgotPasswordCode.getCode())
                .append("\n\nCảm ơn bạn dã sử dụng dịch vụ của Town Hose")
                .append("\n\nTrân trọng, \nTown house team");

        mailService.sendEmail(user.getEmail(), emailSubject, emailContent);
        return CommonFunction.successOutput(email);
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
        for (ForgotPasswordCode forgotPasswordCode : forgotPasswordCodes) {
            if (forgotPasswordCode.getCode().equalsIgnoreCase(forgotPasswordInputDTO.getCode())) {
                User user = userRepository.findByEmail(forgotPasswordCode.getEmail()).orElseThrow(()
                        -> new ResourceNotFoundException("User", "email", forgotPasswordCode.getEmail()));
                if (forgotPasswordCode.getExpirationDate().before(clock.now())) {
                    throw new ResourceInvalidInputException("Code has been expiration");
                }
                if (!forgotPasswordCode.isUsed()) {
                    if (encoder.matches(forgotPasswordInputDTO.getNewPassword(), user.getPassword())) {
                        throw new ResourceInvalidInputException("New passwords are not allowed to match with the current password");
                    }
                    if (forgotPasswordInputDTO.getNewPassword().equalsIgnoreCase(forgotPasswordInputDTO.getConfirmNewPassword())) {
                        forgotPasswordCode.setUsed(true);
                        forgotPasswordCodeRepository.save(forgotPasswordCode);
                        changePassword(user, forgotPasswordInputDTO.getNewPassword());
                        return CommonFunction.successOutput(userRepository.save(user));
                    }
                    throw new ResourceInvalidInputException("Two password's input do not match");
                }
                throw new ResourceInvalidInputException("Code has been used");
            }
        }
        throw new ResourceNotFoundException("Không tìm thấy user");
    }

    @Override
    public BaseOutput getListBookingHistories(Long userId) {
        try {
            List<Booking> listBookingHistories = bookingRepository.getListBookingHistory(userId);
            return CommonFunction.successOutput(listBookingHistories);
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceNotFoundException("Không tìm thấy user với id: " + userId);
        }
    }


    private User changePassword(User user, String newPassword) {
        user.setPassword(encoder.encode(newPassword));
        return user;
    }

    private User sendEmailRemindForChangedPassword(User user) {
        String emailSubject = "Mật khẩu của bạn đã được thay đổi";

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Xin chào " + user.getFullName())
                .append("\n\nChúng tôi muốn cho bạn biết rằng, mật khẩu Town House của bạn đã được thay đổi.")
                .append("\n\nNếu bạn không làm điều này, vui lòng thực hiện chức năng quên mật khẩu.")
                .append("\n\nNếu bạn đã gặp bất kỳ vấn đề gì, xin vui lòng liên hệ với bộ phận hỗ trợ của chúng tôi.")
                .append("\n\nBạn không nên trả lời lại email này. Chúng tôi sẽ không bao giờ yêu cầu mật khẩu của " +
                        "bạn và chúng tôi không khuyến khích bạn chia sẻ nó với bất kỳ ai.")
                .append("\n\nTrân trọng, \nTown house team");
        mailService.sendEmail(user.getEmail(), emailSubject, emailContent);
        return user;
    }

    @Override
    public void saveDeviceToken(User user, String deviceToken) {
        Set<UserDeviceToken> deviceTokens = new HashSet<>();
        UserDeviceToken userDeviceToken = new UserDeviceToken(deviceToken);
        userDeviceToken.setUser(user);
        deviceTokenRepository.save(userDeviceToken);
    }

    public void storeToken(Token token) {
        logger.info("TokenServiceImpl.storeToken");
        tokenRepository.save(token);
    }
}
