package vn.com.pn.screen.m002Host.service;

import org.springframework.web.multipart.MultipartFile;
import vn.com.pn.screen.m002Host.dto.HostDTO;
import vn.com.pn.screen.m002Host.dto.HostSearchDTO;
import vn.com.pn.screen.m006HostCategory.dto.HostDiscountDTO;
import vn.com.pn.screen.m002Host.dto.HostUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m002Host.entity.Host;
import vn.com.pn.screen.m001User.entity.User;

import java.util.List;

public interface HostService {
    BaseOutput getAll(Integer pageNo, Integer pageSize, String sortBy);

    Host insert(HostDTO hostDTO, User user);

    BaseOutput discountHostPrice(HostDiscountDTO hostDiscountDTO);

    BaseOutput update(HostUpdateDTO hostUpdateDTO);

    BaseOutput delete(String hostId);

    BaseOutput getId(String id);

    BaseOutput approve(String id);

    BaseOutput search(String searchText, int pageNo);

    BaseOutput getByCityId(String cityId, Integer pageNo, Integer pageSize, String sortBy);

    BaseOutput recommendHost();

    BaseOutput getLookDates(Long userId);
}
