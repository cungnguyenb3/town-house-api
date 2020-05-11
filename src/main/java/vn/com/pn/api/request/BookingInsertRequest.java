package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingInsertRequest {
    private String hostId;
    private String checkInDate;
    private String checkOutDate;
    private String pricePerNight;
    private String nights;
    private String roomPrice;
    private String cleanCosts;
    private String serviceCharge;
    private String totalPrice;
    private String guests;
    private String numberOfAdultGuest;
    private String numberOfChildrenGuest;
    private String numberOfInfantGuest;
}
