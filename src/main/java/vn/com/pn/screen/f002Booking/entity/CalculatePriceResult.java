package vn.com.pn.screen.f002Booking.entity;

import lombok.Getter;
import lombok.Setter;

import org.joda.time.LocalDate;

@Getter
@Setter
public class CalculatePriceResult {
    private String startDate;
    private String endDate;
    private Long pricePerNight;
    private Long nights;
    private Long totalPrice;
    private Integer guests;
    private Long priceWithoutCleanCostsAndServiceCharge;
    private Long cleanCosts;
    private Long serviceCharge;
    private Integer numberOfAdultGuest;
    private Integer numberOfChildrenGuest;
    private Integer numberOfInfantGuest;
    private Long hostId;
}
