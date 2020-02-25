package vn.com.pn.service.hostcity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostCityDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.HostCity;
import vn.com.pn.repository.hostcity.HostCityRepository;
import vn.com.pn.service.host.HostServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class HostCityServiceImpl implements HostCityService{
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);

    @Autowired
    private HostCityRepository hostCityRepository;

    @Override
    public BaseOutput getAll() {
        List<Object> listHostCities = new ArrayList<Object>(hostCityRepository.findAll());
        return CommonFunction.successOutput(listHostCities);
    }

    @Override
    public BaseOutput insert(HostCityDTO hostCityDTO) {
        try {
            HostCity hostCity = new HostCity();
            if (hostCityDTO.getName() != null && hostCityDTO.getName() != ""){
                hostCity.setName(hostCityDTO.getName());
            }
            return CommonFunction.successOutput(hostCityRepository.save(hostCity));
        } catch (Exception e) {
            logger.trace(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
