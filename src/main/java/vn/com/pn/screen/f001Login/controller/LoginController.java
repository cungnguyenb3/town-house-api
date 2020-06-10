package vn.com.pn.screen.f001Login.controller;

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
import vn.com.pn.screen.f001Login.request.UserLoginRequest;
import vn.com.pn.api.response.JwtResponse;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.exception.ResourceUnauthorizedException;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.security.JwtProvider;
import vn.com.pn.screen.m001User.service.UserPrinciple;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
public class LoginController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Value("Authorization")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @ApiOperation(value = "Login with username and password", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_SIGN_IN, method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginRequest loginRequest) {
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
            logger.info("========== LoginController.login END ==========");
            return ResponseEntity.ok(new JwtResponse(jwt, userPrinciple));
        }catch (Exception e) {
            BaseOutput baseOutput = new BaseOutput();
            baseOutput.setStatus(401);
            baseOutput.setData(null);
            baseOutput.setMessage("Username hoặc password không chính xác! ");
            return ResponseEntity.badRequest().body(baseOutput);
        }
    }

    @ApiOperation(value = "Refresh token", response = BaseOutput.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
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

    @ExceptionHandler({ResourceUnauthorizedException.class})
    public ResponseEntity<String> handleAuthenticationException(ResourceUnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
