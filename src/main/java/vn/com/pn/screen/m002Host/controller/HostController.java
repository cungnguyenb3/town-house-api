package vn.com.pn.screen.m002Host.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.screen.m002Host.request.HostDiscountRequest;
import vn.com.pn.screen.m002Host.request.HostRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m002Host.dto.HostDTO;
import vn.com.pn.screen.m006HostCategory.dto.HostDiscountDTO;
import vn.com.pn.screen.m002Host.dto.HostUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.security.AuthService;
import vn.com.pn.screen.m002Host.service.HostService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "hosts", description = "Manage Host")
public class HostController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private HostService hostService;

    @Autowired
    private AuthService authService;

    @ApiOperation(value = "View a list hosts", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "15") Integer pageSize,
                             @RequestParam(defaultValue = "id") String sortBy) {
        logger.info("========== HostController.getAll START ==========");
        BaseOutput response = hostService.getAll(pageNo, pageSize, sortBy);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostController.getAll END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    @ApiOperation(value = "Add a host", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody HostRequest request) {
        logger.info("========== HostController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            User userLogin = authService.getLoggedUser();
            HostDTO hostDTO = MapperUtil.mapper(request, HostDTO.class);
            BaseOutput response = hostService.insert(hostDTO, userLogin);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== HostController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Search host")
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_SEARCH, method = RequestMethod.GET)
    public BaseOutput search(@RequestParam(value = "name", required = false) String name) {
        logger.info("========== HostController.search START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(name));
        int pageNo = 0;
        if (name != null ){
            pageNo = 1;
        }
        BaseOutput response = hostService.search(name, pageNo);
        logger.info("========== HostController.search END ==========");
        return response;
    }

    @ApiOperation(value = "Get host by city id")
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_GET_BY_CITY, method = RequestMethod.GET)
    public BaseOutput getByCity(@PathVariable String id,
                                @RequestParam(defaultValue = "0") Integer pageNo,
                                @RequestParam(defaultValue = "15") Integer pageSize,
                                @RequestParam(defaultValue = "id") String sortBy) {
        logger.info("========== HostController.getByCity START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        BaseOutput response = new BaseOutput();
        response.setData(hostService.getByCityId(id, pageNo, pageSize, sortBy));
        logger.info("========== HostController.getByCity END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    @ApiOperation(value = "Update host discount from agent")
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_DISCOUNT, method = RequestMethod.POST)
    public BaseOutput updateHostDiscount(@RequestBody HostDiscountRequest request) {
        logger.info("========== HostController.updateHostDiscount START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            HostDiscountDTO hostDiscountDTO = MapperUtil.mapper(request, HostDiscountDTO.class);
            BaseOutput response = hostService.discountHostPrice(hostDiscountDTO);
            return response;
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    @ApiOperation(value = "Update a host", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ID, method = RequestMethod.PUT)
    public BaseOutput update(@RequestBody HostRequest request) {
        logger.info("========== HostController.update START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            HostUpdateDTO hostUpdateDTO = MapperUtil.mapper(request, HostUpdateDTO.class);
            BaseOutput response = hostService.update(hostUpdateDTO);
            logger.info("========== HostController.update END ==========");
            return response;
        }
        catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    @ApiOperation(value = "Delete a host", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ID, method = RequestMethod.DELETE)
    public BaseOutput delete(@Valid @PathVariable String id){
        logger.info("========== HostController.delete START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        try {
            BaseOutput response = hostService.delete(id);
            logger.info("========== HostController.delete END ==========");
            return response;
        }
        catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Get a host by host id", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ID, method = RequestMethod.GET)
    public ResponseEntity<?> getId (@Valid @PathVariable String id) {
        logger.info("========== HostController.delete START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        logger.info("========== HostController.delete END ==========");
        BaseOutput response = hostService.getId(id);
        return ResponseEntity.ok(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Approve host by host id", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_APPROVED, method = RequestMethod.PUT)
    public BaseOutput approveHost(@Valid @PathVariable String id) {
        logger.info("========== HostController.approveHost START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        logger.info("========== HostController.approveHost END ==========");
        BaseOutput response = hostService.approve(id);  
        return CommonFunction.successOutput(response);
    }
}
