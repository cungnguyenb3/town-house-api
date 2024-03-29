package vn.com.pn.screen.f003Review.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.screen.f003Review.request.HostReviewRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.f003Review.dto.HostReviewDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m001User.repository.UserRepository;
import vn.com.pn.security.AuthService;
import vn.com.pn.screen.f003Review.service.HostReviewService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "HostReviews", description = "Manage Host Reviews")
public class HostReviewController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private HostReviewService hostReviewService;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "View a list host reviews", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_REVIEW_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== HostReviewController.getAll START ==========");
        BaseOutput response = hostReviewService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostReviewController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Add a host review", response = BaseOutput.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_REVIEW_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody HostReviewRequest request, BindingResult bindingResult) {
        logger.info("========== HostReviewController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        if (bindingResult.hasErrors()) {
            return CommonFunction.errorValidateItem(bindingResult);
        }
        User userLogin = userRepository.findById(authService.getLoggedUser().getId()).orElse(null);
        HostReviewDTO hostReviewDTO = MapperUtil.mapper(request, HostReviewDTO.class);
        BaseOutput response = hostReviewService.insert(hostReviewDTO, userLogin);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostReviewController.insert END ==========");
        return response;
    }

    @ApiOperation(value = "Get list reviews of host", response = BaseOutput.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_REVIEW_GET_BY_HOST_ID, method = RequestMethod.GET)
    public BaseOutput getHostReviewByHostId(@Valid @PathVariable String id) {
        logger.info("========== HostReviewController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        BaseOutput response = hostReviewService.getHostReviewByHost(id);
        return response;
    }
}
