package vn.com.pn.service.host;

import vn.com.pn.common.dto.HostDTO;
import vn.com.pn.common.dto.HostDiscountDTO;
import vn.com.pn.common.dto.HostUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.Host;
import vn.com.pn.domain.User;

import java.util.List;

public interface HostService {
    BaseOutput getAll(Integer pageNo, Integer pageSize, String sortBy);
    BaseOutput insert(HostDTO hostDTO, User user);
    BaseOutput discountHostPrice (HostDiscountDTO hostDiscountDTO);
    BaseOutput update(HostUpdateDTO hostUpdateDTO);
    BaseOutput delete(String hostId);
    BaseOutput getId(String id);
    BaseOutput approve(String id);
    List<Host> search(String searchText, int pageNo);
    BaseOutput getByCityId(String cityId, Integer pageNo, Integer pageSize, String sortBy);
}
