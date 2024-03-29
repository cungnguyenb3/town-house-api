package vn.com.pn.screen.m012Language.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.screen.m012Language.request.LanguageRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m012Language.dto.LanguageDTO;
import vn.com.pn.screen.m012Language.dto.LanguageUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m012Language.service.LanguageService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "language", description = "Manage language")
public class LanguageController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private LanguageService languageService;

    @ApiOperation(value = "View a list language", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_LANGUAGE_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== LanguageController.getAll START ==========");
        BaseOutput response = languageService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== LanguageController.getAll END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Add a new language", response = BaseOutput.class)
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_LANGUAGE_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody LanguageRequest request) {
        logger.info("========== LanguageController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));

        LanguageDTO languageDTO = MapperUtil.mapper(request, LanguageDTO.class);
        BaseOutput response = languageService.insert(languageDTO);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== LanguageController.insert END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Update a language", response = BaseOutput.class)
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_LANGUAGE_ID, method = RequestMethod.PUT)
    public BaseOutput update(@Valid @PathVariable String id, @Valid @RequestBody LanguageRequest request) {
        logger.info("========== LanguageController.update START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));

        LanguageUpdateDTO languageUpdateDTO = MapperUtil.mapper(request, LanguageUpdateDTO.class);
        languageUpdateDTO.setId(id);
        BaseOutput response = languageService.update(languageUpdateDTO);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== LanguageController.update END ==========");
        return response;
    }
}
