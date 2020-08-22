package vn.com.pn.screen.m004HostCity.service;

import vn.com.pn.screen.m004HostCity.dto.HostCityDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostCityService {
    BaseOutput getAll();

    BaseOutput insert(HostCityDTO hostCityDTO);

    BaseOutput update(HostCityDTO dto, String id);

    BaseOutput delete(String id);
}
