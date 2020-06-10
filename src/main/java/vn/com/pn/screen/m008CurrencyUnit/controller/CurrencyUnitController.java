package vn.com.pn.screen.m008CurrencyUnit.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.com.pn.screen.m008CurrencyUnit.request.CurrencyUnitRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m008CurrencyUnit.dto.CurrencyUnitDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m008CurrencyUnit.service.CurrencyUnitService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "Currency Unit", description = "Manage Currency Unit")
public class CurrencyUnitController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private CurrencyUnitService currencyUnitService;

    @ApiOperation(value = "View a list currency unit", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.CURRENCY_UNIT_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== CurrencyUnitController.getAll START ==========");
        BaseOutput response = currencyUnitService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== CurrencyUnitController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Add a new currency unit", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.CURRENCY_UNIT_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody CurrencyUnitRequest request) {
        logger.info("========== CurrencyUnitController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            CurrencyUnitDTO currencyUnitDTO = MapperUtil.mapper(request, CurrencyUnitDTO.class);
            BaseOutput response = currencyUnitService.insert(currencyUnitDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== CurrencyUnitController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
