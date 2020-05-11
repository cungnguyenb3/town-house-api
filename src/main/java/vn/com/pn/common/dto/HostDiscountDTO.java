package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostDiscountDTO {
    private String discountPercent;
    private String startDiscountDay;
    private String endDiscountDay;
    private String hostId;
}
