package vn.com.pn.service.hostagent;

import vn.com.pn.common.dto.HostAgentDTO;
import vn.com.pn.common.dto.HostAgentUpdateDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostAgentService {
    BaseOutput getAll();
    BaseOutput getId(String id);
    BaseOutput insert(HostAgentDTO hostAgentDTO);
    BaseOutput update(HostAgentUpdateDTO hostAgentUpdateDTO);
    BaseOutput delete(String hostAgentId);
}
