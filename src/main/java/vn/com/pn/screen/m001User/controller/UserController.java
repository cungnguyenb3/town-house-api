package vn.com.pn.screen.m001User.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.dto.*;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m001User.request.*;
import vn.com.pn.security.AuthService;
import vn.com.pn.security.JwtProvider;
import vn.com.pn.security.JwtUserDetailsServiceImpl;
import vn.com.pn.screen.m001User.service.UserPrinciple;
import vn.com.pn.screen.m001User.service.UserService;
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
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
//                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "View a list users", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "15") Integer pageSize,
                             @RequestParam(defaultValue = "id") String sortBy) {
        logger.info("========== UserController.getAll START ==========");
        BaseOutput response = userService.getAll(pageNo, pageSize, sortBy);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== UserController.getAll END ==========");
        return response;
    }

//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
//                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Get a user with an Id", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ID, method = RequestMethod.GET)
    public ResponseEntity<?> getId(@Valid @PathVariable String id) {
        logger.info("========== UserController.getId START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        BaseOutput response = userService.getId(id);
        logger.info("======= UserController.getId END========");
        return ResponseEntity.ok(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Delete an user", response = BaseOutput.class)
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ID, method = RequestMethod.DELETE)
    public BaseOutput delete(@Valid @PathVariable String id) {
        logger.info("========== UserController.delete START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        User userLogin = authService.getLoggedUser();
        BaseOutput response = userService.delete(id, userLogin);
        logger.info("========== UserController.delete END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Register a new user", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_SIGN_UP, method = RequestMethod.POST)
    public BaseOutput registerUser(@Valid @RequestBody UserRequest request) {
        logger.info("========== UserController.register START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        boolean isRegisterAdmin = false;
        UserDTO userDTO = MapperUtil.mapper(request, UserDTO.class);
        BaseOutput response = userService.insert(userDTO, isRegisterAdmin);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== UserController.register END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @ApiOperation(value = "Register a new admin", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_SIGN_UP_ADMIN, method = RequestMethod.POST)
    public BaseOutput registerAdmin(@Valid @RequestBody UserRequest request) {
        logger.info("========== UserController.registerAdmin START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        boolean isRegisterAdmin = true;
        UserDTO userDTO = MapperUtil.mapper(request, UserDTO.class);
        BaseOutput response = userService.insert(userDTO, isRegisterAdmin);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== UserController.register END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Change the password", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_CHANGE_PASSWORD, method = RequestMethod.PUT)
    public BaseOutput changePassword(@Valid @PathVariable String id, @RequestBody UserChangePasswordRequest request) {
        logger.info("========== UserController.changePassword START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        UserChangePasswordDTO userChangePasswordDTO = MapperUtil.mapper(request, UserChangePasswordDTO.class);
        userChangePasswordDTO.setId(id);
        BaseOutput response = userService.changePassword(userChangePasswordDTO);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== UserController.changePassword END ==========");
        return response;
    }

    @ApiOperation(value = "Update a user profile", response = BaseOutput.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ID, method = RequestMethod.PUT)
    public BaseOutput update(@Valid @RequestBody UserUpdateRequest request) {
        logger.info("========== UserController.update START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        UserUpdateDTO userUpdateDTO = MapperUtil.mapper(request, UserUpdateDTO.class);
        User userLogin = authService.getLoggedUser();
        BaseOutput response = userService.update(userLogin, userUpdateDTO);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== UserController.update END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Update list host wishlist", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_CHANGE_HOST_WISH_LIST, method = RequestMethod.PUT)
    public BaseOutput updateHostWishList(@Valid @PathVariable String id, @RequestBody UserUpdateHostWishListRequest request) {
        logger.info("========== UserController.updateHostWishList START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        UserUpdateWishListDTO userUpdateWishListDTO = MapperUtil.mapper(request, UserUpdateWishListDTO.class);
        userUpdateWishListDTO.setId(id);
        userUpdateWishListDTO.setHostIds(request.getHostId());
        BaseOutput response = userService.updateWishListHost(userUpdateWishListDTO);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== UserController.updateHostWishList END ==========");
        return response;
    }

    @ApiOperation(value = "Active user with send email", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ACTIVATION, method = RequestMethod.GET)
    public String authenticateUser(@RequestParam(value = "token") String token) {
        logger.info("========== UserController.authenticateUser START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(token));
        try {
            String userNameFromJwtToken = jwtProvider.getUserNameFromJwtToken(token);
            UserPrinciple userPrinciple = (UserPrinciple) userDetailsService.loadUserByUsername(userNameFromJwtToken);
            userService.enableUser(userPrinciple);
            logger.info("========== UserController.authenticateUser START ==========");
            return "Tài khoản của bạn đã xác nhận thành công!";
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE);
            return "Tài khoản xác nhận thất bại!";
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_UPDATE_PASSWORD_WITH_CODE, method = RequestMethod.PUT)
    public BaseOutput getForgotPasswordCode(@Valid @RequestBody ForgotPasswordCodeRequest request) {
        logger.info("========== UserController.sendForgotPasswordCode START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        ForgotPasswordInputDTO forgotPasswordInputDTO = MapperUtil.mapper(request, ForgotPasswordInputDTO.class);
        logger.info("========== UserController.sendForgotPasswordCode END ==========");
        return userService.handleForgotPassword(forgotPasswordInputDTO);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Get lists booking histories from user", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_BOOKING_HISTORIES, method = RequestMethod.GET)
    public BaseOutput getListBookingHistories() {
        logger.info("========== UserController.getListBookingHistories START ==========");
        User userLogin = authService.getLoggedUser();
        BaseOutput response = userService.getListBookingHistories(userLogin.getId());
        logger.info("========== UserController.getListBookingHistories END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_SEND_FORGOT_CODE_VIA_EMAIL, method = RequestMethod.POST)
    public BaseOutput sendForgotPasswordCode(@Valid @RequestBody UserSendForgotPasswordRequest request) {
        BaseOutput response = userService.forgotPassword(request.getEmail());
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Get lists hosts from user", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_HOST, method = RequestMethod.GET)
    public BaseOutput getListHostByUser(@RequestParam(defaultValue = "0") Integer pageNo,
                                        @RequestParam(defaultValue = "15") Integer pageSize,
                                        @RequestParam(defaultValue = "id") String sortBy) {
        logger.info("========== UserController.getListHostByUser START ==========");
        User user = authService.getLoggedUser();

        BaseOutput response = userService.getListHostByUser(pageNo, pageSize,sortBy, user.getId());

        return response;
    }
}