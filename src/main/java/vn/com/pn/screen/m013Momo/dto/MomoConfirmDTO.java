package vn.com.pn.screen.m013Momo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MomoConfirmDTO {
    private String partnerCode;
    private String partnerRefId;
    private String requestType;
    private String requestId;
    private String momoTransId;
    private String signature;

    @Override
    public String toString() {
        return "MomoConfirmDTO{" +
                "partnerCode='" + partnerCode + '\'' +
                ", partnerRefId='" + partnerRefId + '\'' +
                ", requestType='" + requestType + '\'' +
                ", requestId='" + requestId + '\'' +
                ", momoTransId='" + momoTransId + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
