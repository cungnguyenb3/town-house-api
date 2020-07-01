package vn.com.pn.screen.f002Booking.service;

import vn.com.pn.screen.f002Booking.dto.BookingCalculatePriceDTO;
import vn.com.pn.screen.f002Booking.dto.BookingDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.entity.User;

public interface BookingService {
    BaseOutput getAll();

    BaseOutput insert(BookingDTO bookingDTO, User userLogin);

    BaseOutput calculatePrice(BookingCalculatePriceDTO bookingCalculatePriceDTO);

    BaseOutput confirmBookingRequest(String bookingId, long userId);

    BaseOutput confirmBookingPaid(String bookingId);
}
