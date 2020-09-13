package vn.com.pn.screen.f003Review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HostReviewByHostResDTO {
    private String stars;
    private List<HostReviewWithUserDTO> listReview;
}
