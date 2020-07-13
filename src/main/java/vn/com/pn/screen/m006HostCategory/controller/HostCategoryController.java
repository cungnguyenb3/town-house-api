package vn.com.pn.screen.m006HostCategory.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.screen.m006HostCategory.request.HostCategoryRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m006HostCategory.dto.HostCategoryDTO;
import vn.com.pn.screen.m006HostCategory.dto.HostCategoryUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m006HostCategory.service.HostCategoryService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "hosts", description = "Manage Host Category")
public class HostCategoryController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private HostCategoryService hostCategoryService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "View list host categories", response = BaseOutput.class)
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_HOST_CATEGORY_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== HostCategoryController.getAll START ==========");
        BaseOutput response = hostCategoryService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostCategoryController.getAll END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "View a host category with id", response = BaseOutput.class)
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_HOST_CATEGORY_ID, method = RequestMethod.GET)
    public BaseOutput getById(@Valid @PathVariable String id) {
        logger.info("========== HostCategoryController.getById START ==========");
        BaseOutput response = hostCategoryService.getById(id);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostCategoryController.getById END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Add a new host category", response = BaseOutput.class)
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_HOST_CATEGORY_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody HostCategoryRequest request) {
        logger.info("========== HostCategoryController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        HostCategoryDTO hostCategoryDTO = MapperUtil.mapper(request, HostCategoryDTO.class);
        BaseOutput response = hostCategoryService.insert(hostCategoryDTO);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostCategoryController.insert END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Update a host category", response = BaseOutput.class)
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_HOST_CATEGORY_ID, method = RequestMethod.PUT)
    public BaseOutput update(@Valid @PathVariable String id, @Valid @RequestBody HostCategoryRequest request) {
        logger.info("========== HostCategoryController.update START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        HostCategoryUpdateDTO hostCategoryUpdateDTO = MapperUtil.mapper(request, HostCategoryUpdateDTO.class);
        hostCategoryUpdateDTO.setId(id);
        BaseOutput response = hostCategoryService.update(hostCategoryUpdateDTO);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostCategoryController.update END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Delete a host category", response = BaseOutput.class)
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_HOST_CATEGORY_ID, method = RequestMethod.DELETE)
    public BaseOutput delete(@Valid @PathVariable String id) {
        logger.info("========== HostCategoryController.delete START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        BaseOutput response = hostCategoryService.delete(id);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostCategoryController.delete END ==========");
        return response;
    }
}
