package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostReviewDTO {
    private String hostId;
    private String bookingId;
    private String content;
    private String starRating;
    private String isDelete;
    private String isOutstanding;
}
