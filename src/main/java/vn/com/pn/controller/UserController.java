package vn.com.pn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import vn.com.pn.api.request.*;
import vn.com.pn.api.response.JwtResponse;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.UserChangePasswordDTO;
import vn.com.pn.common.dto.UserDTO;
import vn.com.pn.common.dto.UserUpdateDTO;
import vn.com.pn.common.dto.UserUpdateWishListDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.security.JwtProvider;
import vn.com.pn.security.JwtUserDetailsServiceImpl;
import vn.com.pn.service.user.UserPrinciple;
import vn.com.pn.service.user.UserService;
import vn.com.pn.utils.MapperUtil;

//https://dzone.com/articles/spring-boot-restful-api-documentation-with-swagger

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "users", description = "Manage User")
public class UserController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Value("Authorization")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

    @ApiOperation(value = "View a list users", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== UserController.getAll START ==========");
        BaseOutput response = userService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== UserController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Get a user with an Id", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ID, method = RequestMethod.GET)
    public BaseOutput getId(@PathVariable String id) {
        logger.info("========== UserController.getId START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        try {
            BaseOutput response = userService.getId(id);
            logger.info("======= UserController.getId ========");
            return response;
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Delete an user", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ID, method = RequestMethod.DELETE)
    public BaseOutput delete(@Valid @PathVariable String id) {
        logger.info("========== UserController.delete START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        try {
            BaseOutput response = userService.delete(id);
            logger.info("========== UserController.delete END ==========");
            return response;
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @PostMapping("/users/signup")
    public BaseOutput registerUser(@Valid @RequestBody UserInsertRequest request) {
        logger.info("========== UserController.getAll START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            UserDTO userDTO = MapperUtil.mapper(request,UserDTO.class);
            BaseOutput response = userService.insert(userDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== UserController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @PostMapping("/users/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserPrinciple userPrinciple = jwtProvider.getUserFromLogin(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt, userPrinciple));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    @RequestMapping(value = "/users/refreshToken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        if (jwtProvider.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtProvider.refreshToken(token);
            return ResponseEntity.ok(CommonFunction.successOutput(new JwtResponse(refreshedToken)));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ApiOperation(value = "Change the password", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_CHANGE_PASSWORD, method = RequestMethod.PUT)
    public BaseOutput changePassword(@Valid @PathVariable String id, @RequestBody UserChangePasswordRequest request) {
        logger.info("========== UserController.changePassword START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            UserChangePasswordDTO userChangePasswordDTO = MapperUtil.mapper(request, UserChangePasswordDTO.class);
            userChangePasswordDTO.setId(id);
            BaseOutput response = userService.changePassword(userChangePasswordDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== UserController.changePassword END ==========");
            return response;
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE);
            return CommonFunction.failureOutput();
        }
    }

    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ID, method = RequestMethod.PUT)
    public BaseOutput update(@Valid @PathVariable String id, @RequestBody UserUpdateRequest request) {
        logger.info("========== UserController.update START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            UserUpdateDTO userUpdateDTO = MapperUtil.mapper(request, UserUpdateDTO.class);
            userUpdateDTO.setId(id);
            BaseOutput response = userService.update(userUpdateDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== UserController.update END ==========");
            return response;
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE);
            return CommonFunction.failureOutput();
        }
    }

    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_CHANGE_HOST_WISH_LIST, method = RequestMethod.PUT)
    public BaseOutput updateHostWishList(@Valid @PathVariable String id, @RequestBody UserUpdateHostWishListRequest request) {
        logger.info("========== UserController.updateHostWishList START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            UserUpdateWishListDTO userUpdateWishListDTO = MapperUtil.mapper(request, UserUpdateWishListDTO.class);
            userUpdateWishListDTO.setId(id);
            userUpdateWishListDTO.setHostIds(request.getHostId());
            BaseOutput response = userService.updateWishListHost(userUpdateWishListDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== UserController.updateHostWishList END ==========");
            return response;
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE);
            return CommonFunction.failureOutput();
        }
    }

    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ACTIVATION, method = RequestMethod.GET)
    public String authenticateUser(@RequestParam(value = "token") String token) {
        logger.info("========== UserController.updateHostWishList START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(token));
        try {
            String userNameFromJwtToken = jwtProvider.getUserNameFromJwtToken(token);
            UserPrinciple userPrinciple = (UserPrinciple) userDetailsService.loadUserByUsername(userNameFromJwtToken);
            userService.enableUser(userPrinciple);
            return "Tài khoản của bạn đã xác nhận thành công!";
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE);
            return "Tài khoản xác nhận thất bại!";
        }
    }
}