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
import vn.com.pn.api.request.ProcedureCheckInRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.LanguageDTO;
import vn.com.pn.common.dto.ProcedureCheckInDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.service.procedurecheckin.ProcedureCheckInService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "procedure check in", description = "Manage procedure check in")
public class ProcedureCheckInController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private ProcedureCheckInService procedureCheckInService;

    @ApiOperation(value = "View a list procedure check in", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.PROCEDURE_CHECK_IN, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== ProcedureCheckInController.getAll START ==========");
        BaseOutput response = procedureCheckInService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== ProcedureCheckInController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Add a new procedure check in", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.PROCEDURE_CHECK_IN, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody ProcedureCheckInRequest request) {
        logger.info("========== ProcedureCheckInController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            ProcedureCheckInDTO procedureCheckInDTO = MapperUtil.mapper(request, ProcedureCheckInDTO.class);
            BaseOutput response = procedureCheckInService.insert(procedureCheckInDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== ProcedureCheckInController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}