package vn.com.pn.screen.m004HostCity.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.exception.ResourceInvalidInputException;
import vn.com.pn.screen.m004HostCity.dto.HostCityDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m004HostCity.entity.HostCity;
import vn.com.pn.screen.m004HostCity.repository.HostCityRepository;
import vn.com.pn.screen.m002Host.service.HostServiceImpl;

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
}
