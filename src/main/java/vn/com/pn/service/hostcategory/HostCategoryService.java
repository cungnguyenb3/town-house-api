package vn.com.pn.service.hostcategory;

import vn.com.pn.common.dto.HostCategoryDTO;
import vn.com.pn.common.dto.HostCategoryUpdateDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostCategoryService {
    BaseOutput getAll();
    BaseOutput insert(HostCategoryDTO hostCategoryDTO);
    BaseOutput update(HostCategoryUpdateDTO hostCategoryUpdateDTO);
    BaseOutput delete(String id);
    BaseOutput getById(String id);
}
