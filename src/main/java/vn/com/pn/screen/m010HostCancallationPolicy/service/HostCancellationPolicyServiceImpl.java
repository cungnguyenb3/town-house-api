package vn.com.pn.screen.m010HostCancallationPolicy.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m010HostCancallationPolicy.dto.HostCancellationPolicyDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m010HostCancallationPolicy.entity.HostCancellationPolicy;
import vn.com.pn.screen.m010HostCancallationPolicy.repository.HostCancellationPolicyRepository;
import vn.com.pn.screen.m002Host.service.HostServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class HostCancellationPolicyServiceImpl implements HostCancellationPolicyService {
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);

    @Autowired
    private HostCancellationPolicyRepository hostCancellationPolicyRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("HostCancellationPolicyServiceImpl.getAll");
        List<Object> listHostCategories = new ArrayList<Object>(hostCancellationPolicyRepository.findAll());
        return CommonFunction.successOutput(listHostCategories);
    }

    @Override
    public BaseOutput insert(HostCancellationPolicyDTO hostCancellationPolicyDTO) {
        logger.info("HostCancellationPolicyServiceImpl.insert");
        try {
            HostCancellationPolicy hostCancellationPolicy = new HostCancellationPolicy();
            if (hostCancellationPolicyDTO.getName() != null && hostCancellationPolicyDTO.getName() != ""){
                hostCancellationPolicy.setName(hostCancellationPolicyDTO.getName());
            }
            if (hostCancellationPolicyDTO.getDescription() != null && hostCancellationPolicyDTO.getDescription() != ""){
                hostCancellationPolicy.setDescription(hostCancellationPolicyDTO.getDescription());
            }
            return CommonFunction.successOutput(hostCancellationPolicyRepository.save(hostCancellationPolicy));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
