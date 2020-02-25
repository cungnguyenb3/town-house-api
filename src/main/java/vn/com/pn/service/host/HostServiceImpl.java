package vn.com.pn.service.host;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.*;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.repository.host.HostRepository;
import vn.com.pn.repository.hostagent.HostAgentRepository;
import vn.com.pn.repository.hostcategory.HostCategoryRepository;
import vn.com.pn.repository.hostcity.HostCityRepository;
import vn.com.pn.repository.hostroomtype.HostRoomTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class HostServiceImpl implements HostService {
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private HostAgentRepository hostAgentRepository;

    @Autowired
    private HostCategoryRepository hostCategoryRepository;

    @Autowired
    private HostRoomTypeRepository hostRoomTypeRepository;

    @Autowired
    private HostCityRepository hostCityRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("HostServiceImpl.getAll");
        List<Object> listHost = new ArrayList<>(hostRepository.findAll());
        return CommonFunction.successOutput(listHost);
    }

    @Override
    public BaseOutput insert(HostDTO hostDTO) {
        try {
            Host host = new Host();
            if (hostDTO.getName() != null && hostDTO.getName() != ""){
                host.setName(hostDTO.getName());
            }
            if (hostDTO.getDescription() != null && hostDTO.getDescription() != ""){
                host.setDescription(hostDTO.getDescription());
            }
            if (hostDTO.getHostAgentId() != null && hostDTO.getHostAgentId() != ""){
                HostAgent hostAgent = hostAgentRepository.findById(Integer.parseInt(hostDTO.getHostAgentId())).orElseThrow(()
                        -> new ResourceNotFoundException("HostAgent","id", hostDTO.getHostAgentId()));
                host.setHostAgent(hostAgent);
            }
            if (hostDTO.getHostCategoryId() != null && hostDTO.getHostCategoryId() != ""){
                HostCategory hostCategory = hostCategoryRepository.findById(Integer.parseInt(hostDTO.getHostCategoryId())).orElseThrow(()
                        -> new ResourceNotFoundException("HostCategory","id", hostDTO.getHostCategoryId()));
                host.setHostCategory(hostCategory);
            }
            if (hostDTO.getHostRoomTypeId() != null && hostDTO.getHostRoomTypeId() != ""){
                HostRoomType hostRoomType = hostRoomTypeRepository.findById(Integer.parseInt(hostDTO.getHostRoomTypeId())).orElseThrow(()
                        -> new ResourceNotFoundException("HostRoomType","id", hostDTO.getHostRoomTypeId()));
                host.setHostRoomType(hostRoomType);
            }
            if (hostDTO.getHostCityId() != null && hostDTO.getHostCityId() != ""){
                HostCity hostCity = hostCityRepository.findById(Integer.parseInt(hostDTO.getHostCityId())).orElseThrow(()
                        -> new ResourceNotFoundException("HostCity","id", hostDTO.getHostCityId()));
                host.setHostCity(hostCity);
            }

            return CommonFunction.successOutput(hostRepository.save(host));
        } catch (Exception e) {
            logger.trace(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
