package vn.com.pn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.api.request.HostInsertRequest;
import vn.com.pn.api.request.HostUpdateRequest;
import vn.com.pn.api.request.UserInsertRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostDTO;
import vn.com.pn.common.dto.HostUpdateDTO;
import vn.com.pn.common.dto.UserDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.Host;
import vn.com.pn.service.host.HostService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "hosts", description = "Manage Host")
public class HostController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private HostService hostService;

    @ApiOperation(value = "View a list hosts", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== HostController.getAll START ==========");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("-------------AUTHENTICATION ---------");
        System.out.println(authentication);
        BaseOutput response = hostService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Add a host", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody HostInsertRequest request) {
        logger.info("========== HostController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            HostDTO hostDTO = MapperUtil.mapper(request, HostDTO.class);
            BaseOutput response = hostService.insert(hostDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== UserController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ID, method = RequestMethod.PUT)
    public BaseOutput update(@RequestBody HostUpdateRequest request) {
        logger.info("========== HostController.update START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            HostUpdateDTO hostUpdateDTO = MapperUtil.mapper(request, HostUpdateDTO.class);
            BaseOutput response = hostService.update(hostUpdateDTO);
            logger.info("========== UserController.update END ==========");
            return response;
        }
        catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }


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
}
