package vn.com.pn.screen.f002Booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingAnalyzeDTO {
    private int totalBooking;
    private int numberOfCancel;
    private int numberOfHappening;
    private int numberOfComplete;
    private double totalRevenue;
}
