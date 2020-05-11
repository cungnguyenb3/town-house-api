package vn.com.pn.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class CalculatePriceResult {
    private LocalDate startDate;
    private LocalDate endDate;
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
}
