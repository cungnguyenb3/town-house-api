package vn.com.pn.screen.m007HostRoomType.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.exception.ResourceInvalidInputException;
import vn.com.pn.screen.m007HostRoomType.dto.HostRoomTypeDTO;
import vn.com.pn.screen.m007HostRoomType.dto.HostRoomTypeUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m007HostRoomType.entity.HostRoomType;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.screen.m007HostRoomType.repository.HostRoomTypeRepository;
import vn.com.pn.screen.m002Host.service.HostServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class HostRoomTypeServiceImpl implements HostRoomTypeService {
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);

    @Autowired
    private HostRoomTypeRepository hostRoomTypeRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("HostRoomTypeServiceImpl.getAll");
        List<Object> listHostRoomTypes = new ArrayList<Object>(hostRoomTypeRepository.findAll());
        return CommonFunction.successOutput(listHostRoomTypes);
    }

    @Override
    public BaseOutput insert(HostRoomTypeDTO hostRoomTypeDTO) {
        logger.info("HostRoomTypeServiceImpl.insert");
        try {
            HostRoomType hostRoomType = new HostRoomType();
            if (hostRoomTypeDTO.getName() == null && hostRoomTypeDTO.getName().trim().length() == 0) {
                throw new ResourceInvalidInputException("Vui lòng nhập tên loại phòng!");
            }
            hostRoomType.setName(hostRoomTypeDTO.getName());
            if (hostRoomTypeDTO.getDescription() != null && hostRoomTypeDTO.getDescription() != "") {
                hostRoomType.setDescription(hostRoomTypeDTO.getDescription());
            }
            return CommonFunction.successOutput(hostRoomTypeRepository.save(hostRoomType));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    @Override
    public BaseOutput update(HostRoomTypeUpdateDTO hostRoomTypeUpdateDTO) {
        logger.info("HostRoomTypeServiceImpl.update");
        try {
            HostRoomType hostRoomType = hostRoomTypeRepository.findById(
                    Long.parseLong(hostRoomTypeUpdateDTO.getId())).orElseThrow(
                    () -> new ResourceNotFoundException("Host Room Type", "id", hostRoomTypeUpdateDTO.getId()));
            ;
            if (hostRoomTypeUpdateDTO.getName() != null && hostRoomTypeUpdateDTO.getName() != "") {
                hostRoomType.setName(hostRoomTypeUpdateDTO.getName());
            }
            if (hostRoomTypeUpdateDTO.getDescription() != null && hostRoomTypeUpdateDTO.getDescription() != "") {
                hostRoomType.setDescription(hostRoomTypeUpdateDTO.getDescription());
            }
            return CommonFunction.successOutput(hostRoomTypeRepository.save(hostRoomType));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    @Override
    public BaseOutput delete(String id) {
        logger.info("HostRoomTypeServiceImpl.delete");
        HostRoomType hostRoomType = hostRoomTypeRepository.findById(Long.parseLong(id)).orElse(null);
        if (hostRoomType == null) {
            throw new ResourceNotFoundException("Host room type", "id", id);
        }
        hostRoomTypeRepository.delete(hostRoomType);
        Object object = ResponseEntity.ok().build();
        return CommonFunction.successOutput(object);
    }
}
