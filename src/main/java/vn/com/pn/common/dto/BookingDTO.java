package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDTO {
    private String hostId;
    private String checkInDate;
    private String checkOutDate;
    private String pricePerDay;
    private String priceForStay;
    private String taxPaid;
    private String amountPaid;
    private String cancelDate;
    private String isRefund;
    private String refundPaid;
    private String effectiveAmount;
    private String bookingDate;
    private String status;
    private String note;
}
