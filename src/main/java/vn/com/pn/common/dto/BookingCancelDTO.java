package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingCancelDTO {
    private String id;
    private String status;
    private String userId;
}
