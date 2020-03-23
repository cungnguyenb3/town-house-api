package vn.com.pn.service.user;

import vn.com.pn.common.dto.UserChangePasswordDTO;
import vn.com.pn.common.dto.UserDTO;
import vn.com.pn.common.dto.UserUpdateDTO;
import vn.com.pn.common.dto.UserUpdateWishListDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.User;

public interface UserService {
    BaseOutput getAll();
    BaseOutput getId(String userId);
    BaseOutput insert(UserDTO userDTO);
    BaseOutput update(UserUpdateDTO userUpdateDTO);
    BaseOutput delete(String userId);
    BaseOutput changePassword(UserChangePasswordDTO userChangePasswordDTO);
    BaseOutput updateWishListHost(UserUpdateWishListDTO userUpdateWishListDTO);
}
