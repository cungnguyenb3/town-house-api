package vn.com.pn.screen.m007HostRoomType.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.screen.m007HostRoomType.request.HostRoomTypeRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m007HostRoomType.dto.HostRoomTypeDTO;
import vn.com.pn.screen.m007HostRoomType.dto.HostRoomTypeUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m007HostRoomType.service.HostRoomTypeService;
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
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_HOST_ROOM_TYPE_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== HostRoomTypeController.getAll START ==========");
        BaseOutput response = hostRoomTypeService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostRoomTypeController.getAll END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Add a host room type", response = BaseOutput.class)
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_HOST_ROOM_TYPE_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody HostRoomTypeRequest request) {
        logger.info("========== HostRoomTypeController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));

        HostRoomTypeDTO hostRoomTypeDTO = MapperUtil.mapper(request, HostRoomTypeDTO.class);
        BaseOutput response = hostRoomTypeService.insert(hostRoomTypeDTO);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostRoomTypeController.insert END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Update a host room type", response = BaseOutput.class)
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_HOST_ROOM_TYPE_ID, method = RequestMethod.PUT)
    public BaseOutput update(@Valid @PathVariable String id, @Valid @RequestBody HostRoomTypeRequest request) {
        logger.info("========== HostRoomTypeController.update START ==========");
        HostRoomTypeUpdateDTO hostRoomTypeUpdateDTO = MapperUtil.mapper(request, HostRoomTypeUpdateDTO.class);
        hostRoomTypeUpdateDTO.setId(id);
        BaseOutput response = hostRoomTypeService.update(hostRoomTypeUpdateDTO);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostRoomTypeController.update END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Delete a host room type", response = BaseOutput.class)
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_HOST_ROOM_TYPE_ID, method = RequestMethod.DELETE)
    public BaseOutput update(@Valid @PathVariable String id) {
        logger.info("========== HostRoomTypeController.delete START ==========");
        BaseOutput response = hostRoomTypeService.delete(id);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostRoomTypeController.update END ==========");
        return response;
    }
}
