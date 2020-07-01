package vn.com.pn.screen.m004HostCity.service;

import vn.com.pn.screen.m004HostCity.dto.HostCityDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostCityService {
    BaseOutput getAll();

    BaseOutput insert(HostCityDTO hostCityDTO);
}
