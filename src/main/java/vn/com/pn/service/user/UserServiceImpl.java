package vn.com.pn.service.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.UserDTO;
import vn.com.pn.common.dto.UserUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.repository.user.UserRepository;
import vn.com.pn.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static Log logger = LogFactory.getLog(UserServiceImpl.class);

//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public BaseOutput getAll() {
//        logger.info("UserServiceImpl.getAll");
//        List<Object> listUser = new ArrayList<Object>(userRepository.findAll());
//        return CommonFunction.successOutput(listUser);
//    }
//
//    @Override
//    public BaseOutput getId(String userId) {
//        logger.info("UserServiceImpl.getId");
//        try {
//            Object user = (Object) userRepository.findById(Integer.parseInt(userId));
//            if (user != Optional.empty()){
//                return CommonFunction.successOutput(user);
//            }
//            return CommonFunction.failureOutput();
//        } catch (Exception e){
//            logger.trace(ScreenMessageConstants.FAILURE, e);
//            return CommonFunction.failureOutput();
//        }
//    }
//
//    @Override
//    public BaseOutput insert(UserDTO userDTO) {
//        logger.info("UserService.insert");
//        try {
//            User user = new User();
//            if (userDTO.getName() != null && userDTO.getName() != ""){
//                user.setName(userDTO.getName());
//            }
//            if (userDTO.getEmail() != null && userDTO.getEmail() != ""){
//                user.setEmail(userDTO.getEmail());
//            }
//            if (userDTO.getPhone() != null && userDTO.getPhone() != ""){
//                user.setPhone(userDTO.getPhone());
//            }
//            if (userDTO.getPassword() != null && userDTO.getPassword() != ""){
//                String generatedPasswordHash = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(12));
//                user.setPassword(generatedPasswordHash);
//            }
//            return CommonFunction.successOutput(userRepository.save(user));
//        } catch (Exception e) {
//            logger.trace(ScreenMessageConstants.FAILURE, e);
//            return CommonFunction.failureOutput();
//        }
//
//    }
//
//    @Override
//    public BaseOutput update(UserUpdateDTO userUpdateDTO) {
//        logger.info("UserService.insert");
//        User user = userRepository.findById(Integer.parseInt(userUpdateDTO.getId())).orElseThrow(()
//                -> new ResourceNotFoundException("User","id", userUpdateDTO.getId()));
//        if (userUpdateDTO.getName() != null && userUpdateDTO.getName() != ""){
//            user.setName(userUpdateDTO.getName());
//        }
//        if (userUpdateDTO.getEmail() != null && userUpdateDTO.getEmail() != ""){
//            user.setEmail(userUpdateDTO.getEmail());
//        }
//        if (userUpdateDTO.getPhone() != null && userUpdateDTO.getPhone() != ""){
//            user.setPhone(userUpdateDTO.getPhone());
//        }
//        if (userUpdateDTO.getPassword() != null && userUpdateDTO.getPassword() != ""){
//            String generatedPasswordHash = BCrypt.hashpw(userUpdateDTO.getPassword(), BCrypt.gensalt(12));
//            user.setPassword(generatedPasswordHash);
//        }
//        return CommonFunction.successOutput(userRepository.save(user));
//    }
//
//    @Override
//    public BaseOutput delete(String userId) {
//        logger.info("UserService.delete");
//        User user = userRepository.findById(Integer.parseInt(userId)).orElseThrow(()
//                -> new ResourceNotFoundException("User","id", userId));
//        userRepository.delete(user);
//        Object object = ResponseEntity.ok().build();
//        return CommonFunction.successOutput(object);
//    }
}
