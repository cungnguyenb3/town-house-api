package vn.com.pn.screen.f002Booking.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {
    private String hostId;
    private String startDate;
    private String endDate;
    private String pricePerNight;
    private String nights;
    private String priceWithoutCleanCostsAndServiceCharge;
    private String cleanCosts;
    private String serviceCharge;
    private String totalPrice;
    private String guests;
    private String numberOfAdultGuest;
    private String numberOfChildrenGuest;
    private String numberOfInfantGuest;
}
