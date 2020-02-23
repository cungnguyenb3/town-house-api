package vn.com.pn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import vn.com.pn.api.request.*;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.UserDTO;
import vn.com.pn.common.dto.UserUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.service.user.UserService;
import vn.com.pn.utils.MapperUtil;
//https://dzone.com/articles/spring-boot-restful-api-documentation-with-swagger

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "users", description = "Manage Product")
public class UserController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private UserService userService;

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
        } catch (Exception e){
            logger.trace(ScreenMessageConstants.FAILURE,e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Add an user", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody UserInsertRequest request) {
        logger.info("========== UserController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            UserDTO userDTO = MapperUtil.mapper(request, UserDTO.class);
            BaseOutput response = userService.insert(userDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== UserController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.trace(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Update an user", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ID, method = RequestMethod.PUT)
    public BaseOutput update(@Valid @PathVariable String id, @RequestBody UserUpdateRequest request){
        logger.info("========== UserController.update START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            UserUpdateDTO userUpdateDTO = MapperUtil.mapper(request, UserUpdateDTO.class);
            userUpdateDTO.setId(id);
            BaseOutput response = userService.update(userUpdateDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== UserController.update END ==========");
            return response;
        }
        catch (Exception e){
            logger.trace(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Delete an user", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.USER_ID, method = RequestMethod.DELETE)
    public BaseOutput delete(@Valid @PathVariable String id){
        logger.info("========== UserController.delete START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        try {
            BaseOutput response = userService.delete(id);
            logger.info("========== UserController.delete END ==========");
            return response;
        }
        catch (Exception e) {
            logger.trace(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}