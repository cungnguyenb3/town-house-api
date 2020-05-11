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
import vn.com.pn.api.request.RuleRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.LanguageDTO;
import vn.com.pn.common.dto.RuleDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.service.rule.RuleService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "rule", description = "Manage rule")
public class RuleController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private RuleService ruleService;

    @ApiOperation(value = "View a list rules", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.RULE_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== RuleController.getAll START ==========");
        BaseOutput response = ruleService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== RuleController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Add a new rule", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.LANGUAGE_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody RuleRequest request) {
        logger.info("========== RuleController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            RuleDTO ruleDTO = MapperUtil.mapper(request, RuleDTO.class);
            BaseOutput response = ruleService.insert(ruleDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== RuleController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
