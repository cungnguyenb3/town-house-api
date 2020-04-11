package vn.com.pn.service.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.UserChangePasswordDTO;
import vn.com.pn.common.dto.UserDTO;
import vn.com.pn.common.dto.UserUpdateDTO;
import vn.com.pn.common.dto.UserUpdateWishListDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.*;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.repository.host.HostRepository;
import vn.com.pn.repository.role.RoleRepository;
import vn.com.pn.repository.user.UserRepository;
import vn.com.pn.security.JwtProvider;
import vn.com.pn.service.mail.MailService;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static Log logger = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public BaseOutput getAll() {
        logger.info("UserServiceImpl.getAll");
        List<Object> listUser = new ArrayList<Object>(userRepository.findAll());
        return CommonFunction.successOutput(listUser);
    }

    @Override
    public BaseOutput getId(String userId) {
        logger.info("UserServiceImpl.getId");
        try {
            Object user = (Object) userRepository.findById(Integer.parseInt(userId));
            if (user != Optional.empty()){
                return CommonFunction.successOutput(user);
            }
            return CommonFunction.failureOutput();
        } catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @Override
    public BaseOutput delete(String userId) {
        logger.info("UserService.delete");
        User user = userRepository.findById(Integer.parseInt(userId)).orElseThrow(()
                -> new ResourceNotFoundException("User","id", userId));
        userRepository.delete(user);
        Object object = ResponseEntity.ok().build();
        return CommonFunction.successOutput(object);
    }

    @Override
    public BaseOutput insert (UserDTO userDTO) {
        logger.info("UserService.insert");
        try {
            if(userRepository.existsByUsername(userDTO.getUsername())) {
                return CommonFunction.errorLogic(400,"Fail -> Username is already taken!");
            }
            if(userRepository.existsByEmail(userDTO.getEmail())) {
                return CommonFunction.errorLogic(400,"Fail -> Email is already in use!");
            }
            User user = userRepository.save(getInsertUserInfo(userDTO));
            sendEmailConfirm(userDTO);
            return CommonFunction.successOutput(user);
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @Override
    public BaseOutput update(UserUpdateDTO userUpdateDTO) {
        logger.info("UserService.update");
        try {
            User user = userRepository.findById(Integer.parseInt(userUpdateDTO.getId())).orElseThrow(()
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
        logger.info("UserService.changePassword");
        try {
            User user = userRepository.findById(Integer.parseInt(userChangePasswordDTO.getId())).orElseThrow(()
                    -> new  ResourceNotFoundException("User", "id", userChangePasswordDTO.getId()));
            if (userChangePasswordDTO.getCurrentPassword() != null && userChangePasswordDTO.getCurrentPassword() != ""
                && userChangePasswordDTO.getNewPassword() != null && userChangePasswordDTO.getNewPassword() != ""
                    && userChangePasswordDTO.getConfirmNewPassword() != null && userChangePasswordDTO.getConfirmNewPassword() != ""
            ) {
                if (user.getPassword().equalsIgnoreCase(encoder.encode(userChangePasswordDTO.getCurrentPassword()))) {
                    if (userChangePasswordDTO.getNewPassword().equalsIgnoreCase(userChangePasswordDTO.getConfirmNewPassword())){
                        user.setPassword(userChangePasswordDTO.getNewPassword());
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
        try {
            User user = userRepository.findById(Integer.parseInt(userUpdateWishListDTO.getId())).orElseThrow(()
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
                    Host host = hostRepository.findById(Integer.parseInt(hostId)).orElseThrow(()
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

    private User getInsertUserInfo (UserDTO userDTO){
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
        user.setEnable(false);
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
        roles.add(userRole);
        user.setRoles(roles);
        return user;
    }

    private String generateTokenForActiveUser (String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateJwtToken(authentication);
    }

    private void sendEmailConfirm(UserDTO userDTO){
        try {
            String token = generateTokenForActiveUser(userDTO.getUsername(), userDTO.getPassword());
            String emailSubject = "Xác thực địa chỉ email";
            String greeting = "Xin chào " + userDTO.getFullName() + ", \n";
            String thanks = "Cảm ơn bạn đã đăng ký sử dụng dịch vụ tại town house. " +
                    "Vui lòng click vào link bên dưới để hoàn tất đăng ký tài khoản. \n" ;
            String localhostUrl = "http://localhost:8080/api/users/activation?token="+token;

            //For deploy on heroku
            String herokuUrl = "https://town-house-api-seven-team.herokuapp.com/api/users/activation?token="+token;

            StringBuffer emailContent = new StringBuffer();
            emailContent.append(greeting).append(thanks).append(localhostUrl);
            mailService.sendEmail(userDTO.getEmail(), emailSubject, emailContent);

        } catch (MailException mailException) {
            logger.error(ScreenMessageConstants.FAILURE, mailException);
        }
    }

    @Override
    public BaseOutput enableUser(UserPrinciple userPrinciple) {
        logger.info("UserService.enableUser");
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
}
