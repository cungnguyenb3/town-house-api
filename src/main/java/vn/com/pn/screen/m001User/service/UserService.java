package vn.com.pn.screen.m001User.service;

import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.dto.*;
import vn.com.pn.screen.m001User.entity.User;

public interface UserService {
    BaseOutput getAll(Integer pageNo, Integer pageSize, String sortBy);

    BaseOutput getId(String userId);

    BaseOutput insert(UserDTO userDTO, boolean isRegisterAdmin);

    BaseOutput update(User user, UserUpdateDTO userUpdateDTO);

    BaseOutput delete(String userId, User userLogin);

    BaseOutput changePassword(UserChangePasswordDTO userChangePasswordDTO);

    BaseOutput updateWishListHost(UserUpdateWishListDTO userUpdateWishListDTO);

    BaseOutput enableUser(UserPrinciple userPrinciple);

    BaseOutput forgotPassword(String email);

    BaseOutput handleForgotPassword(ForgotPasswordInputDTO forgotPasswordInputDTO);

    BaseOutput getListBookingHistories(Long userId);

    BaseOutput getListHostByUser(Integer pageNo, Integer pageSize, String sortBy, Long userId);
}
