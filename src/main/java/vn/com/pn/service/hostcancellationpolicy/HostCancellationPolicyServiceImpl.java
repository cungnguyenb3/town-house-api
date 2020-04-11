package vn.com.pn.service.hostcancellationpolicy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostCancellationPolicyDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.HostCancellationPolicy;
import vn.com.pn.repository.hostcancellationpolicy.HostCancellationPolicyRepository;
import vn.com.pn.service.host.HostServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class HostCancellationPolicyServiceImpl implements HostCancellationPolicyService {
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);
    private HostCancellationPolicyRepository hostCancellationPolicyRepository;

    @Override
    public BaseOutput getAll() {
        List<Object> listHostCategories = new ArrayList<Object>(hostCancellationPolicyRepository.findAll());
        return CommonFunction.successOutput(listHostCategories);
    }

    @Override
    public BaseOutput insert(HostCancellationPolicyDTO hostCancellationPolicyDTO) {
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
