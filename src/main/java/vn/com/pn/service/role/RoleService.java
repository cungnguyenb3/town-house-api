package vn.com.pn.service.role;

import vn.com.pn.common.dto.RoleDTO;
import vn.com.pn.common.output.BaseOutput;

public interface RoleService {
    BaseOutput getAll();
    BaseOutput insert(RoleDTO roleDTO);
}
