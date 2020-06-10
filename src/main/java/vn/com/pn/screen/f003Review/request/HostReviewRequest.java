package vn.com.pn.screen.f003Review.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostReviewRequest {
    private String bookingId;
    private String content;
    private String starRating;
}
