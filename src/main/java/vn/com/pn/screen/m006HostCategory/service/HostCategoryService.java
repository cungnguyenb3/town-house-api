package vn.com.pn.screen.m006HostCategory.service;

import vn.com.pn.screen.m006HostCategory.dto.HostCategoryDTO;
import vn.com.pn.screen.m006HostCategory.dto.HostCategoryUpdateDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostCategoryService {
    BaseOutput getAll();

    BaseOutput insert(HostCategoryDTO hostCategoryDTO);

    BaseOutput update(HostCategoryUpdateDTO hostCategoryUpdateDTO);

    BaseOutput delete(String id);

    BaseOutput getById(String id);
}
