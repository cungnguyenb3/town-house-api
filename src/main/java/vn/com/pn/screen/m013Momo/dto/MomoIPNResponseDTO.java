package vn.com.pn.screen.m013Momo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MomoIPNResponseDTO {
    private Integer status;
    private String message;
    private String partnerRefId;
    private String momoTransId;
    private Long amount;
    private String signature;
}
