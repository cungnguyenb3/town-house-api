package vn.com.pn.service.hostcategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostCategoryDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.Host;
import vn.com.pn.domain.HostAgent;
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
        List<Object> listHostCategories = new ArrayList<Object>(hostCategoryRepository.findAll());
        return CommonFunction.successOutput(listHostCategories);
    }

    @Override
    public BaseOutput insert(HostCategoryDTO hostCategoryDTO) {
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
}
