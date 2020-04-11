package vn.com.pn.service.hostcancellationpolicy;

import vn.com.pn.common.dto.HostCancellationPolicyDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostCancellationPolicyService {
    BaseOutput getAll();
    BaseOutput insert(HostCancellationPolicyDTO hostCancellationPolicyDTO);
}
