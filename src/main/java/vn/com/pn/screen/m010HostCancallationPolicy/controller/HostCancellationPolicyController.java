package vn.com.pn.screen.m010HostCancallationPolicy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.com.pn.screen.m010HostCancallationPolicy.request.HostCancellationPolicyRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m010HostCancallationPolicy.dto.HostCancellationPolicyDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m010HostCancallationPolicy.service.HostCancellationPolicyService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "host-cancellation-policies", description = "Manage Host Cancellation Policy")
public class HostCancellationPolicyController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private HostCancellationPolicyService hostCancellationPolicyService;

    @ApiOperation(value = "View list host cancellation policies", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_CANCELLATION_POLICY_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== HostCancellationPolicyController.getAll START ==========");
        BaseOutput response = hostCancellationPolicyService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostCancellationPolicyController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Add an host cancellation policies", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_CANCELLATION_POLICY_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody HostCancellationPolicyRequest request) {
        logger.info("========== HostCancellationPolicyController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            HostCancellationPolicyDTO hostCancellationPolicyDTO = MapperUtil.mapper(request, HostCancellationPolicyDTO.class);
            BaseOutput response = hostCancellationPolicyService.insert(hostCancellationPolicyDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== HostCancellationPolicyController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
