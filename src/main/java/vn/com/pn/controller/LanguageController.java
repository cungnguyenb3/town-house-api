package vn.com.pn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.com.pn.api.request.LanguageRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostCityDTO;
import vn.com.pn.common.dto.LanguageDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.service.language.LanguageService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "language", description = "Manage language")
public class LanguageController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private LanguageService languageService;

    @ApiOperation(value = "View a list list language", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.LANGUAGE_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== LanguageController.getAll START ==========");
        BaseOutput response = languageService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== LanguageController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Add a new language", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.LANGUAGE_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody LanguageRequest request) {
        logger.info("========== LanguageController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            LanguageDTO languageDTO = MapperUtil.mapper(request, LanguageDTO.class);
            BaseOutput response = languageService.insert(languageDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== LanguageController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
