package vn.com.pn.screen.f001Login.controller;

import io.jsonwebtoken.Clock;
import io.jsonwebtoken.impl.DefaultClock;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.screen.f001Login.request.UserLoginRequest;
import vn.com.pn.api.response.JwtResponse;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.exception.ResourceUnauthorizedException;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m001User.repository.UserRepository;
import vn.com.pn.screen.m001User.request.UserLogoutRequest;
import vn.com.pn.screen.m001User.service.UserService;
import vn.com.pn.security.AuthService;
import vn.com.pn.security.JwtProvider;
import vn.com.pn.screen.m001User.service.UserPrinciple;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
public class LoginController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Value("Authorization")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private Clock clock = DefaultClock.INSTANCE;

    @ApiOperation(value = "Login with username and password", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_SIGN_IN, method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@RequestBody UserLoginRequest loginRequest) {
        logger.info("========== LoginController.login START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(loginRequest));
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateJwtToken(authentication);
            UserPrinciple userPrinciple = jwtProvider.getUserFromLogin(authentication);
            if (userPrinciple != null && loginRequest.getDeviceToken() != null
                    && !loginRequest.getDeviceToken().isEmpty() && !loginRequest.getDeviceToken().equalsIgnoreCase("string")) {
                userService.saveDeviceToken(userPrinciple.getUser(), loginRequest.getDeviceToken());
            }
            if (jwt != null && !jwt.isEmpty()) {
                userService.saveToken(jwt, userPrinciple.getUser().getId(), jwtProvider.calculateExpirationDate(clock.now()));
            }
            logger.info("========== LoginController.login END ==========");
            return ResponseEntity.ok(new JwtResponse(jwt, userPrinciple));
        } catch (Exception e) {
            throw new ResourceUnauthorizedException("Tên đăng nhập hoặc mật khẩu không chính xác");
        }
    }

    @ApiOperation(value = "Refresh token", response = BaseOutput.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @RequestMapping(value = "/users/refreshToken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        logger.info("========== LoginController.refreshToken START ==========");
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        logger.info("========== LoginController.refreshToken END ==========");
        if (jwtProvider.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtProvider.refreshToken(token);
            return ResponseEntity.ok(CommonFunction.successOutput(new JwtResponse(refreshedToken)));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ApiOperation(value = "Get user via token", response = BaseOutput.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ME, method = RequestMethod.GET)
    public ResponseEntity<?> getUserViaToken() {
        logger.info("========== LoginController.getUserViaToken START ==========");
        if (authService.getLoggedUser() == null) {
            throw new ResourceUnauthorizedException("Token nhập vào đã hết hạn hoặc không chính xác!");
        }
        else {
            User userLogin = userRepository.findById(authService.getLoggedUser().getId()).orElse(null);
            return ResponseEntity.ok().body(userLogin);
        }

    }

    @ApiOperation(value = "Logout user account", response = BaseOutput.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_SIGN_OUT, method = RequestMethod.POST)
    public ResponseEntity<?> logout(@RequestHeader(name="Authorization") String tokenHeader, @RequestBody UserLogoutRequest request) {
        logger.info("========== LoginController.logout START ==========");
        String bearer = "Bearer ";

        userService.logout(request, tokenHeader.substring(bearer.length()));
        return ResponseEntity.ok().body("User logout successfully");
    }
}
