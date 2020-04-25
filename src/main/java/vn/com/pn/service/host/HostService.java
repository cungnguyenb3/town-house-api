package vn.com.pn.service.host;

import vn.com.pn.common.dto.HostDTO;
import vn.com.pn.common.dto.HostUpdateDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostService {
    BaseOutput getAll();
    BaseOutput insert(HostDTO hostDTO);
    BaseOutput update(HostUpdateDTO hostUpdateDTO);
    BaseOutput delete(String hostId);
    BaseOutput getId(String id);
}
