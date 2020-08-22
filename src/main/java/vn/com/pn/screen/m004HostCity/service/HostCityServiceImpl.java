package vn.com.pn.screen.m004HostCity.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.exception.ResourceInvalidInputException;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.screen.m004HostCity.dto.HostCityDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m004HostCity.entity.HostCity;
import vn.com.pn.screen.m004HostCity.repository.HostCityRepository;
import vn.com.pn.screen.m002Host.service.HostServiceImpl;
import vn.com.pn.screen.m006HostCategory.dto.HostCategoryUpdateDTO;
import vn.com.pn.screen.m006HostCategory.entity.HostCategory;

import java.util.ArrayList;
import java.util.List;

@Service
public class HostCityServiceImpl implements HostCityService {
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);

    @Autowired
    private HostCityRepository hostCityRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("HostCityServiceImpl.getAll");
        List<Object> listHostCities = new ArrayList<Object>(hostCityRepository.findAll());
        return CommonFunction.successOutput(listHostCities);
    }

    @Override
    public BaseOutput insert(HostCityDTO hostCityDTO) {
        logger.info("HostCityServiceImpl.insert");
        try {
            HostCity hostCity = new HostCity();
            if (hostCityDTO.getName() == null && hostCityDTO.getName().trim().length() == 0) {
                throw new ResourceInvalidInputException("Vui lòng nhập tên thành phố.");
            }
            hostCity.setName(hostCityDTO.getName());
            return CommonFunction.successOutput(hostCityRepository.save(hostCity));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    @Override
    public BaseOutput update(HostCityDTO dto, String id) {
        logger.info("HostCityServiceImpl.insert");
        try {
            HostCity hostCity = hostCityRepository.findById(Long.parseLong(id)).orElseThrow(
                    () -> new ResourceNotFoundException("Host city", "id", id));
            if (dto.getName() == null && dto.getName().trim().length() == 0) {
                throw new ResourceInvalidInputException("Vui lòng nhập tên thành phố.");
            }
            hostCity.setName(dto.getName());
            return CommonFunction.successOutput(hostCityRepository.save(hostCity));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    @Override
    public BaseOutput delete(String id) {
        logger.info("HostCityServiceImpl.insert");
        HostCity hostCity = hostCityRepository.findById(Long.parseLong(id)).orElseThrow(
                () -> new ResourceNotFoundException("HostCity", "id", id)
        );
        hostCityRepository.delete(hostCity);
        Object object = ResponseEntity.ok().build();
        return CommonFunction.successOutput(object);
    }
}
