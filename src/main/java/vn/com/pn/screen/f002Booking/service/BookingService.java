package vn.com.pn.screen.f002Booking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import vn.com.pn.screen.f002Booking.dto.BookingCalculatePriceDTO;
import vn.com.pn.screen.f002Booking.dto.BookingDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.entity.User;

public interface BookingService {
    BaseOutput getAll();

    BaseOutput insert(BookingDTO bookingDTO, User userLogin);

    BaseOutput calculatePrice(BookingCalculatePriceDTO bookingCalculatePriceDTO);

    BaseOutput confirmBookingRequest(String bookingId, long userId) throws JsonProcessingException;

    BaseOutput confirmBookingPaid(String bookingId) throws JsonProcessingException;

    BaseOutput getBookingById(long id);

    ResponseEntity<?> getBookingByCurrentDateAndUser(Long userId);

    BaseOutput getAllBookingFromAgent(long userId);

    BaseOutput getRevenueBooking(long userId);

    BaseOutput cancelBooking(long userId, long bookingId);
}
