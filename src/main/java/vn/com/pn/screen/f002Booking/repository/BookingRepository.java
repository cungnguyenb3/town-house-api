package vn.com.pn.screen.f002Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.pn.screen.f002Booking.entity.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT * FROM bookings WHERE user_id = ?1", nativeQuery = true)
    List<Booking> getListBookingHistory(Long userId);

    @Query(value = "SELECT * FROM bookings WHERE DATE(check_in_date) = CURRENT_DATE OR DATE(check_in_date) > CURRENT_DATE" +
            " OR DATE(check_out_date) = CURRENT_DATE OR DATE(check_out_date) > CURRENT_DATE AND user_id = ?1", nativeQuery = true)
    List<Booking> getBookingByCurrentDateAndUser(Long userId);
}
