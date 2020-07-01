package vn.com.pn.screen.m004HostCity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.com.pn.screen.m004HostCity.request.HostCityRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m004HostCity.dto.HostCityDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m004HostCity.service.HostCityService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "host-city", description = "Manage Host City")
public class HostCityController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private HostCityService hostCityService;

    @ApiOperation(value = "View a list host city", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_CITY_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== HostCityController.getAll START ==========");
        BaseOutput response = hostCityService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostCityController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Add a new host city", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_CITY_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody HostCityRequest request) {
        logger.info("========== HostCityController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));

        HostCityDTO hostCityDTO = MapperUtil.mapper(request, HostCityDTO.class);
        BaseOutput response = hostCityService.insert(hostCityDTO);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostCityController.insert END ==========");
        return response;
    }
}
