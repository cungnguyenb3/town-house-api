package vn.com.pn.service.hostcategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostCategoryDTO;
import vn.com.pn.common.dto.HostCategoryUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.HostCategory;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.repository.hostcategory.HostCategoryRepository;
import vn.com.pn.service.host.HostServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class HostCategoryServiceImpl implements HostCategoryService{
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);

    @Autowired
    private HostCategoryRepository hostCategoryRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("ForgotPasswordCodeServiceImpl.getAll");
        List<Object> listHostCategories = new ArrayList<Object>(hostCategoryRepository.findAll());
        return CommonFunction.successOutput(listHostCategories);
    }

    @Override
    public BaseOutput insert(HostCategoryDTO hostCategoryDTO) {
        logger.info("ForgotPasswordCodeServiceImpl.insert");
        try {
            HostCategory hostCategory = new HostCategory();
            if (hostCategoryDTO.getName() != null && hostCategoryDTO.getName() != ""){
                hostCategory.setName(hostCategoryDTO.getName());
            }
            if (hostCategoryDTO.getDescription() != null && hostCategoryDTO.getDescription() != ""){
                hostCategory.setDescription(hostCategoryDTO.getDescription());
            }
            return CommonFunction.successOutput(hostCategoryRepository.save(hostCategory));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @Override
    public BaseOutput update(HostCategoryUpdateDTO hostCategoryUpdateDTO) {
        logger.info("ForgotPasswordCodeServiceImpl.update");
        try {
            HostCategory hostCategory = hostCategoryRepository.findById(
                    Integer.parseInt(hostCategoryUpdateDTO.getId())).orElseThrow(
                    () -> new ResourceNotFoundException("Host Category", "id",hostCategoryUpdateDTO.getId()));
            if (hostCategoryUpdateDTO.getName() != null && hostCategoryUpdateDTO.getName() != ""){
                hostCategory.setName(hostCategoryUpdateDTO.getName());
            }
            if (hostCategoryUpdateDTO.getDescription() != null && hostCategoryUpdateDTO.getDescription() != ""){
                hostCategory.setDescription(hostCategoryUpdateDTO.getDescription());
            }
            return CommonFunction.successOutput(hostCategoryRepository.save(hostCategory));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @Override
    public BaseOutput delete(String id) {
        logger.info("ForgotPasswordCodeServiceImpl.update");
        HostCategory hostCategory = hostCategoryRepository.findById(Integer.parseInt(id))
                .orElse(null);
        if (hostCategory == null){
            throw new ResourceNotFoundException("Host Category","id",id);
        }
        hostCategoryRepository.delete(hostCategory);
        Object object = ResponseEntity.ok().build();
        return CommonFunction.successOutput(object);
    }
}
