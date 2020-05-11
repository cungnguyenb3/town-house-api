package vn.com.pn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.api.request.HostRoomTypeRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostRoomTypeDTO;
import vn.com.pn.common.dto.HostRoomTypeUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.service.hostroomtype.HostRoomTypeService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "host-room-types", description = "Manage Host Room Type")
public class HostRoomTypeController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private HostRoomTypeService hostRoomTypeService;

    @ApiOperation(value = "View list host room types", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ROOM_TYPE_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== HostRoomTypeController.getAll START ==========");
        BaseOutput response = hostRoomTypeService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostRoomTypeController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Add a host room type", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ROOM_TYPE_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody HostRoomTypeRequest request) {
        logger.info("========== HostRoomTypeController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            HostRoomTypeDTO hostRoomTypeDTO = MapperUtil.mapper(request, HostRoomTypeDTO.class);
            BaseOutput response = hostRoomTypeService.insert(hostRoomTypeDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== HostRoomTypeController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Update a host room type", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ROOM_TYPE_ID, method = RequestMethod.PUT)
    public BaseOutput update(@Valid @PathVariable String id, @Valid @RequestBody HostRoomTypeRequest request) {
        logger.info("========== HostRoomTypeController.update START ==========");
        try {
            HostRoomTypeUpdateDTO hostRoomTypeUpdateDTO = MapperUtil.mapper(request, HostRoomTypeUpdateDTO.class);
            hostRoomTypeUpdateDTO.setId(id);
            BaseOutput response = hostRoomTypeService.update(hostRoomTypeUpdateDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== HostRoomTypeController.update END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiOperation(value = "Delete a host room type", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_ROOM_TYPE_ID, method = RequestMethod.DELETE)
    public BaseOutput update(@Valid @PathVariable String id) {
        logger.info("========== HostRoomTypeController.delete START ==========");
        try {
            BaseOutput response = hostRoomTypeService.delete(id);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== HostRoomTypeController.update END ==========");
            return response;
        }
        catch (Exception e){
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
