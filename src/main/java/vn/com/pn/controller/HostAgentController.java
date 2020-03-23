package vn.com.pn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.api.request.HostAgentRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostAgentDTO;
import vn.com.pn.common.dto.HostAgentUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.service.hostagent.HostAgentService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "hostagents", description = "Manage Host Agent User")
public class HostAgentController {
    private static Log logger = LogFactory.getLog(HostAgentController.class);

    @Autowired
    private HostAgentService hostAgentService;

    @ApiOperation(value = "View a list host-agent", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_AGENT_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== HostAgentController.getAll START ==========");
        BaseOutput response = hostAgentService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostAgentController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Get a host agent with an Id", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_AGENT_ID, method = RequestMethod.GET)
    public BaseOutput getId(@PathVariable String id) {
        logger.info("========== HostAgentController.getId START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        try {
            BaseOutput response = hostAgentService.getId(id);
            logger.info("======= HostAgentController.getId ========");
            return response;
        } catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE,e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Add a new host agent", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_AGENT_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody HostAgentRequest request) {
        logger.info("========== UserController.getAll START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            HostAgentDTO hostAgentDTO = MapperUtil.mapper(request, HostAgentDTO.class);
            BaseOutput response = hostAgentService.insert(hostAgentDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== UserController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Update a host agent", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_AGENT_ID, method = RequestMethod.PUT)
    public BaseOutput update(@Valid @PathVariable String id, @RequestBody HostAgentRequest request) {
        logger.info("========== HostAgentController.update START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            HostAgentUpdateDTO hostAgentUpdateDTO = MapperUtil.mapper(request, HostAgentUpdateDTO.class);
            hostAgentUpdateDTO.setId(id);
            BaseOutput response = hostAgentService.update(hostAgentUpdateDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== HostAgentController.update END ==========");
            return response;
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Delete an host agent", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_AGENT_ID, method = RequestMethod.DELETE)
    public BaseOutput delete(@Valid @PathVariable String id){
        logger.info("========== HostAgentController.delete START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        try {
            BaseOutput response = hostAgentService.delete(id);
            logger.info("========== HostAgentController.delete END ==========");
            return response;
        }
        catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
