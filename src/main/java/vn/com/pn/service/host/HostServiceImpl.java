package vn.com.pn.service.host;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostDTO;
import vn.com.pn.common.dto.HostUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.*;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.repository.host.HostRepository;
import vn.com.pn.repository.hostagent.HostAgentRepository;
import vn.com.pn.repository.hostcategory.HostCategoryRepository;
import vn.com.pn.repository.hostcity.HostCityRepository;
import vn.com.pn.repository.hostroomtype.HostRoomTypeRepository;

import java.math.BigDecimal;
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
            Host host = getInsertHostInfo(hostDTO);
            return CommonFunction.successOutput(hostRepository.save(host));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }

    }

    @Override
    public BaseOutput update(HostUpdateDTO hostUpdateDTO) {
        return null;
    }

    private Host getInsertHostInfo(HostDTO hostDTO) {
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
        if (hostDTO.getAddress() != null && hostDTO.getAddress() != ""){
            host.setAddress(hostDTO.getAddress());
        }
        if (hostDTO.getLatitude() != null && hostDTO.getLatitude() != ""){
            host.setLatitude(hostDTO.getLatitude());
        }
        if (hostDTO.getLongitude() != null && hostDTO.getLongitude() != ""){
            host.setLongitude(hostDTO.getLongitude());
        }
        if (hostDTO.getBedroomCount() != null && hostDTO.getBedroomCount() != ""){
            host.setBedroomCount(Integer.parseInt(hostDTO.getBedroomCount()));
        }
        if (hostDTO.getBed() != null && hostDTO.getBed() != ""){
            host.setBed(Integer.parseInt(hostDTO.getBed()));
        }
        if (hostDTO.getBathroomCount() != null && hostDTO.getBathroomCount() != ""){
            host.setBathroomCount(Integer.parseInt(hostDTO.getBathroomCount()));
        }
        if (hostDTO.getAvailabilityType() != null && hostDTO.getAvailabilityType() != ""){
            if(hostDTO.getAvailabilityType().equals("0")){
                host.setAvailabilityType(false);
            }
            if(hostDTO.getAvailabilityType().equals("1")){
                host.setAvailabilityType(true);
            }
        }
        if (hostDTO.getStartDate() != null && hostDTO.getStartDate() != ""){
            host.setStartDate(CommonFunction.convertStringToDateObject(hostDTO.getStartDate()));
        }
        if (hostDTO.getEndDate() != null && hostDTO.getEndDate() != ""){
            host.setEndDate(CommonFunction.convertStringToDateObject(hostDTO.getEndDate()));
        }
        if (hostDTO.getPrice() != null && hostDTO.getPrice() != ""){
            host.setPrice(new BigDecimal(hostDTO.getPrice()));
        }

        if (hostDTO.getPriceType() != null && hostDTO.getPriceType() != ""){
            if(hostDTO.getPriceType().equals("0")){
                host.setPriceType(false);
            }
            if(hostDTO.getPriceType().equals("1")){
                host.setPriceType(true);
            }
        }
        if (hostDTO.getMinimumStay() != null && hostDTO.getMinimumStay() != ""){
            host.setMinimumStay(hostDTO.getMinimumStay());
        }
        if (hostDTO.getMinimumStayType() != null && hostDTO.getMinimumStayType() != ""){
            if(hostDTO.getMinimumStayType().equals("0")){
                host.setMinimumStayType(false);
            }
            if(hostDTO.getMinimumStayType().equals("1")){
                host.setMinimumStayType(true);
            }
        }
        if (hostDTO.getRefundType() != null && hostDTO.getRefundType() != ""){
            if(hostDTO.getRefundType().equals("0")){
                host.setRefundType(false);
            }
            if(hostDTO.getRefundType().equals("1")){
                host.setRefundType(true);
            }
        }
        if (hostDTO.getStatus() != null && hostDTO.getStatus() != ""){
            if(hostDTO.getStatus().equals("0")){
                host.setStatus(false);
            }
            if(hostDTO.getStatus().equals("1")){
                host.setStatus(true);
            }
        }
        host.setStar(0);
        host.setTotalReview(0);
        return host;
    }

    private Host getUpdateHostInfo(HostUpdateDTO hostUpdateDTO){
        Host host = hostRepository.findById(
                Integer.parseInt(hostUpdateDTO.getId())
        ).orElseThrow(
                () -> new ResourceNotFoundException("Host", "id",hostUpdateDTO.getId())
        );
        if (hostUpdateDTO.getName() != null && hostUpdateDTO.getName() != ""){
            host.setName(hostUpdateDTO.getName());
        }
        if (hostUpdateDTO.getDescription() != null && hostUpdateDTO.getDescription() != ""){
            host.setDescription(hostUpdateDTO.getDescription());
        }
        if (hostUpdateDTO.getHostAgentId() != null && hostUpdateDTO.getHostAgentId() != ""){
            HostAgent hostAgent = hostAgentRepository.findById(Integer.parseInt(hostUpdateDTO.getHostAgentId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostAgent","id", hostUpdateDTO.getHostAgentId()));
            host.setHostAgent(hostAgent);
        }
        if (hostUpdateDTO.getHostCategoryId() != null && hostUpdateDTO.getHostCategoryId() != ""){
            HostCategory hostCategory = hostCategoryRepository.findById(Integer.parseInt(hostUpdateDTO.getHostCategoryId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostCategory","id", hostUpdateDTO.getHostCategoryId()));
            host.setHostCategory(hostCategory);
        }
        if (hostUpdateDTO.getHostRoomTypeId() != null && hostUpdateDTO.getHostRoomTypeId() != ""){
            HostRoomType hostRoomType = hostRoomTypeRepository.findById(Integer.parseInt(hostUpdateDTO.getHostRoomTypeId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostRoomType","id", hostUpdateDTO.getHostRoomTypeId()));
            host.setHostRoomType(hostRoomType);
        }
        if (hostUpdateDTO.getHostCityId() != null && hostUpdateDTO.getHostCityId() != ""){
            HostCity hostCity = hostCityRepository.findById(Integer.parseInt(hostUpdateDTO.getHostCityId())).orElseThrow(()
                    -> new ResourceNotFoundException("HostCity","id", hostUpdateDTO.getHostCityId()));
            host.setHostCity(hostCity);
        }
        if (hostUpdateDTO.getAddress() != null && hostUpdateDTO.getAddress() != ""){
            host.setAddress(hostUpdateDTO.getAddress());
        }
        if (hostUpdateDTO.getLatitude() != null && hostUpdateDTO.getLatitude() != ""){
            host.setLatitude(hostUpdateDTO.getLatitude());
        }
        if (hostUpdateDTO.getLongitude() != null && hostUpdateDTO.getLongitude() != ""){
            host.setLongitude(hostUpdateDTO.getLongitude());
        }
        if (hostUpdateDTO.getBedroomCount() != null && hostUpdateDTO.getBedroomCount() != ""){
            host.setBedroomCount(Integer.parseInt(hostUpdateDTO.getBedroomCount()));
        }
        if (hostUpdateDTO.getBed() != null && hostUpdateDTO.getBed() != ""){
            host.setBed(Integer.parseInt(hostUpdateDTO.getBed()));
        }
        if (hostUpdateDTO.getBathroomCount() != null && hostUpdateDTO.getBathroomCount() != ""){
            host.setBathroomCount(Integer.parseInt(hostUpdateDTO.getBathroomCount()));
        }
        if (hostUpdateDTO.getAvailabilityType() != null && hostUpdateDTO.getAvailabilityType() != ""){
            if(hostUpdateDTO.getAvailabilityType().equals("0")){
                host.setAvailabilityType(false);
            }
            if(hostUpdateDTO.getAvailabilityType().equals("1")){
                host.setAvailabilityType(true);
            }
        }
        if (hostUpdateDTO.getStartDate() != null && hostUpdateDTO.getStartDate() != ""){
            host.setStartDate(CommonFunction.convertStringToDateObject(hostUpdateDTO.getStartDate()));
        }
        if (hostUpdateDTO.getEndDate() != null && hostUpdateDTO.getEndDate() != ""){
            host.setEndDate(CommonFunction.convertStringToDateObject(hostUpdateDTO.getEndDate()));
        }
        if (hostUpdateDTO.getPrice() != null && hostUpdateDTO.getPrice() != ""){
            host.setPrice(new BigDecimal(hostUpdateDTO.getPrice()));
        }
        if (hostUpdateDTO.getPriceType() != null && hostUpdateDTO.getPriceType() != ""){
            if(hostUpdateDTO.getPriceType().equals("0")){
                host.setPriceType(false);
            }
            if(hostUpdateDTO.getPriceType().equals("1")){
                host.setPriceType(true);
            }
        }
        if (hostUpdateDTO.getMinimumStay() != null && hostUpdateDTO.getMinimumStay() != ""){
            host.setMinimumStay(hostUpdateDTO.getMinimumStay());
        }
        if (hostUpdateDTO.getMinimumStayType() != null && hostUpdateDTO.getMinimumStayType() != ""){
            if(hostUpdateDTO.getMinimumStayType().equals("0")){
                host.setMinimumStayType(false);
            }
            if(hostUpdateDTO.getMinimumStayType().equals("1")){
                host.setMinimumStayType(true);
            }
        }
        if (hostUpdateDTO.getRefundType() != null && hostUpdateDTO.getRefundType() != ""){
            if(hostUpdateDTO.getRefundType().equals("0")){
                host.setRefundType(false);
            }
            if(hostUpdateDTO.getRefundType().equals("1")){
                host.setRefundType(true);
            }
        }
        if (hostUpdateDTO.getStatus() != null && hostUpdateDTO.getStatus() != ""){
            if(hostUpdateDTO.getStatus().equals("0")){
                host.setStatus(false);
            }
            if(hostUpdateDTO.getStatus().equals("1")){
                host.setStatus(true);
            }
        }
        return null;
    }

    @Override
    public BaseOutput delete(String hostId) {
        logger.info("HostService.delete");
        Host host = hostRepository.findById(Integer.parseInt(hostId)).orElseThrow(()
                -> new ResourceNotFoundException("Host","id",hostId));
        hostRepository.delete(host);
        Object object = ResponseEntity.ok().build();
        return CommonFunction.successOutput(object);
    }
}
