package vn.com.pn.screen.m003Role.service;

import vn.com.pn.screen.m003Role.dto.RoleDTO;
import vn.com.pn.common.output.BaseOutput;

public interface RoleService {
    BaseOutput getAll();

    BaseOutput insert(RoleDTO roleDTO);
}
