package vn.com.pn.screen.m006HostCategory.dto;

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
