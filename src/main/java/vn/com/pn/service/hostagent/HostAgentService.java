package vn.com.pn.service.hostagent;

import vn.com.pn.common.dto.HostAgentDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostAgentService {
    BaseOutput getAll();
    BaseOutput insert(HostAgentDTO hostAgentDTO);
}
