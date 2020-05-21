package vn.com.pn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import vn.com.pn.common.common.DisplayNameConstant;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.*;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.ForgotPasswordCode;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.security.JwtProvider;
import vn.com.pn.security.JwtUserDetailsServiceImpl;
import vn.com.pn.service.user.UserPrinciple;
import vn.com.pn.service.user.UserService;
import vn.com.pn.utils.MapperUtil;
import vn.com.pn.validate.anotation.Number;

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
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

    @Autowired
    private ApplicationContext context;

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
    public ResponseEntity<?> getId(@PathVariable String id) {
        logger.info("========== UserController.getId START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        BaseOutput response = userService.getId(id);
        logger.info("======= UserController.getId END========");
        return ResponseEntity.ok(response);
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

    @ApiOperation(value = "Register a new user", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_SIGN_UP, method = RequestMethod.POST)
    public BaseOutput registerUser(@Valid @RequestBody UserInsertRequest request) {
        logger.info("========== UserController.register START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            boolean isRegisterAdmin = false;
            UserDTO userDTO = MapperUtil.mapper(request,UserDTO.class);
            BaseOutput response = userService.insert(userDTO, isRegisterAdmin);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== UserController.register END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @ApiOperation(value = "Register a new admin", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_SIGN_UP_ADMIN, method = RequestMethod.POST)
    public BaseOutput registerAdmin(@Valid @RequestBody UserInsertRequest request) {
        logger.info("========== UserController.registerAdmin START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            boolean isRegisterAdmin = true;
            UserDTO userDTO = MapperUtil.mapper(request,UserDTO.class);
            BaseOutput response = userService.insert(userDTO, isRegisterAdmin);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== UserController.register END ==========");
            return response;

        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
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

    @ApiOperation(value = "Update a user profile", response = BaseOutput.class)
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

    @ApiOperation(value = "Update list host wishlist", response = BaseOutput.class)
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

    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_UPDATE_PASSWORD_WITH_CODE, method = RequestMethod.PUT)
    public BaseOutput getForgotPasswordCode (@Valid @RequestBody ForgotPasswordCodeRequest request){
        try {
            logger.info("========== UserController.sendForgotPasswordCode START ==========");
            logger.info("request: " + CommonFunction.convertToJSONString(request));
            ForgotPasswordInputDTO forgotPasswordInputDTO = MapperUtil.mapper(request, ForgotPasswordInputDTO.class);
            logger.info("========== UserController.sendForgotPasswordCode END ==========");
            return userService.handleForgotPassword(forgotPasswordInputDTO);
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE);
            return CommonFunction.failureOutput();
        }
    }

    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_SEND_FORGOT_CODE_VIA_EMAIL, method = RequestMethod.POST)
    public BaseOutput sendForgotPasswordCode (@Valid @RequestBody UserSendForgotPasswordRequest request){
        BaseOutput response = userService.forgotPassword(request.getEmail());
        return response;
    }
}