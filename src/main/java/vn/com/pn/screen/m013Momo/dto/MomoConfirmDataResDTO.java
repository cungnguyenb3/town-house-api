package vn.com.pn.screen.m013Momo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MomoConfirmDataResDTO {
    private String partnerCode;
    private String partnerRefId;
    private String momoTransId;
    private long amount;
    private String signature;
}
