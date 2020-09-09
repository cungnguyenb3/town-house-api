package vn.com.pn.screen.m013Momo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MomoIpnPaymentRequest {
    private String partnerCode;
    private String accessKey;
    private Long amount;
    private String partnerRefId;
    private String partnerTransId;
    private String transType;
    private String momoTransId;
    private Integer status;
    private String message;
    private Long responseTime;
    private String storeId;
    private String signature;
}
