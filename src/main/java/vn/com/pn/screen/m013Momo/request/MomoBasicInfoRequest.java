package vn.com.pn.screen.m013Momo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MomoBasicInfoRequest {
    private String status;
    private String message;
    private String data;
    private String phoneNumber;
    private String bookingCode;
    private String amount;
}
