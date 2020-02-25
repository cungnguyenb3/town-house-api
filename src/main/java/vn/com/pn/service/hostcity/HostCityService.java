package vn.com.pn.service.hostcity;

import vn.com.pn.common.dto.HostCityDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostCityService {
    BaseOutput getAll();
    BaseOutput insert(HostCityDTO hostCityDTO);
}
