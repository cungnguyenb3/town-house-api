package vn.com.pn.screen.m002Host.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HostDiscountRequest {
    private String discountPercent;
    private String startDiscountDay;
    private String endDiscountDay;
    private String hostId;
}
