package vn.com.pn.screen.f003Review.service;

import vn.com.pn.screen.f003Review.dto.HostReviewDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.entity.User;

public interface HostReviewService {
    BaseOutput getAll();

    BaseOutput insert(HostReviewDTO hostReviewDTO, User userLogin);

    BaseOutput getHostReviewByHost(String hostId);
}
