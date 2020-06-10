package vn.com.pn.screen.m003Role.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.com.pn.screen.m003Role.request.RoleRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m003Role.dto.RoleDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m003Role.service.RoleService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "roles", description = "Manage Role")
public class RoleController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "View a list roles", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.ROLE_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== RoleController.getAll START ==========");
        BaseOutput response = roleService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== RoleController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Add an role", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.ROLE_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody RoleRequest request) {
        logger.info("========== RoleController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            RoleDTO roleDTO = MapperUtil.mapper(request, RoleDTO.class);
            BaseOutput response = roleService.insert(roleDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== RoleController.insert END ==========");
            return response;
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
