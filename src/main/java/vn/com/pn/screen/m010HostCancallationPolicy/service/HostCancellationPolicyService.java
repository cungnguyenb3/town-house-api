package vn.com.pn.screen.m010HostCancallationPolicy.service;

import vn.com.pn.screen.m010HostCancallationPolicy.dto.HostCancellationPolicyDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostCancellationPolicyService {
    BaseOutput getAll();
    BaseOutput insert(HostCancellationPolicyDTO hostCancellationPolicyDTO);
}
