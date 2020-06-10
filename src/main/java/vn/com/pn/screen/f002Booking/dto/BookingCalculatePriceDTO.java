package vn.com.pn.screen.f002Booking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingCalculatePriceDTO {
    private String hostId;
    private String startDate;
    private String endDate;
    private String numberOfAdultGuest;
    private String numberOfChildrenGuest;
    private String numberOfInfantGuest;
}
