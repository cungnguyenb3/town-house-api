package vn.com.pn.api.request;

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
