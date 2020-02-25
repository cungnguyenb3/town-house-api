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
import vn.com.pn.api.request.HostCategoryInsertRequest;
import vn.com.pn.api.request.HostRoomTypeInsertRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostCategoryDTO;
import vn.com.pn.common.dto.HostRoomTypeDTO;
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
    public BaseOutput insert(@Valid @RequestBody HostRoomTypeInsertRequest request) {
        logger.info("========== HostCategoryController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            HostRoomTypeDTO hostRoomTypeDTO = MapperUtil.mapper(request, HostRoomTypeDTO.class);
            BaseOutput response = hostRoomTypeService.insert(hostRoomTypeDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== HostCategoryController.insert END ==========");
            return response;
        }
        catch (Exception e){
            logger.trace(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
