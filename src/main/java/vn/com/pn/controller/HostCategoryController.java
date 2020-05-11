package vn.com.pn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.api.request.HostCategoryRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostCategoryDTO;
import vn.com.pn.common.dto.HostCategoryUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.service.hostcategory.HostCategoryService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "hosts", description = "Manage Host Category")
public class HostCategoryController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private HostCategoryService hostCategoryService;

    @ApiOperation(value = "View list host categories", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_CATEGORY_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== HostCategoryController.getAll START ==========");
        BaseOutput response = hostCategoryService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostCategoryController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Add a new host category", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_CATEGORY_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody HostCategoryRequest request) {
        logger.info("========== HostCategoryController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            HostCategoryDTO hostCategoryDTO = MapperUtil.mapper(request, HostCategoryDTO.class);
            BaseOutput response = hostCategoryService.insert(hostCategoryDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== HostCategoryController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Update a host category", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_CATEGORY_ID, method = RequestMethod.PUT)
    public BaseOutput update(@Valid @PathVariable String id, @Valid @RequestBody HostCategoryRequest request) {
        logger.info("========== HostCategoryController.update START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            HostCategoryUpdateDTO hostCategoryUpdateDTO = MapperUtil.mapper(request, HostCategoryUpdateDTO.class);
            hostCategoryUpdateDTO.setId(id);
            BaseOutput response = hostCategoryService.update(hostCategoryUpdateDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== HostCategoryController.update END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Delete a host category", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_CATEGORY_ID, method = RequestMethod.DELETE)
    public BaseOutput delete(@Valid @PathVariable String id) {
        logger.info("========== HostCategoryController.delete START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        try {
            BaseOutput response = hostCategoryService.delete(id);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== HostCategoryController.delete END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
