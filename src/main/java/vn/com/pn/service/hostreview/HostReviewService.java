package vn.com.pn.service.hostreview;

import vn.com.pn.common.dto.HostReviewDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.User;

import java.util.List;

public interface HostReviewService {
    BaseOutput getAll();
    BaseOutput insert(HostReviewDTO hostReviewDTO, User userLogin);
//    BaseOutput getHostReviewByHost(String hostId);
}
