package vn.com.pn.service.hostagent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostAgentDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.HostAgent;
import vn.com.pn.repository.hostagent.HostAgentRepository;
import vn.com.pn.service.user.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class HostAgentServiceImpl implements HostAgentService {
    private static Log logger = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private HostAgentRepository hostAgentRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("UserServiceImpl.getAll");
        List<Object> listHostAgent = new ArrayList<Object>(hostAgentRepository.findAll());
        return CommonFunction.successOutput(listHostAgent);
    }

    @Override
    public BaseOutput insert(HostAgentDTO hostAgentDTO) {
        logger.info("HostAgentService.insert");
        try {
            HostAgent hostAgent = new HostAgent();
            if (hostAgentDTO.getFullName() != null && hostAgentDTO.getFullName() != "") {
                hostAgent.setFullName(hostAgentDTO.getFullName());
            }
            if (hostAgentDTO.getEmail() != null && hostAgentDTO.getEmail() != "") {
                hostAgent.setEmail(hostAgentDTO.getEmail());
            }
            if (hostAgentDTO.getPhone() != null && hostAgentDTO.getPhone() != "") {
                hostAgent.setPhone(hostAgentDTO.getPhone());
            }
            if (hostAgentDTO.getPassword() != null && hostAgentDTO.getPassword() != "") {
                hostAgent.setPassword(hostAgentDTO.getPassword());
            }
            return CommonFunction.successOutput(hostAgentRepository.save(hostAgent));
        } catch (Exception e) {
            logger.trace(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
