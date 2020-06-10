package vn.com.pn.screen.m001User.service;

import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.dto.*;

public interface UserService {
    BaseOutput getAll();
    BaseOutput getId(String userId);
    BaseOutput insert(UserDTO userDTO, boolean isRegisterAdmin);
    BaseOutput update(UserUpdateDTO userUpdateDTO);
    BaseOutput delete(String userId);
    BaseOutput changePassword(UserChangePasswordDTO userChangePasswordDTO);
    BaseOutput updateWishListHost(UserUpdateWishListDTO userUpdateWishListDTO);
    BaseOutput enableUser(UserPrinciple userPrinciple);
    BaseOutput forgotPassword(String email);
    BaseOutput handleForgotPassword (ForgotPasswordInputDTO forgotPasswordInputDTO);
}
