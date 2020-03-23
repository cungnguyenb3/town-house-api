package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingInsertRequest {
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
