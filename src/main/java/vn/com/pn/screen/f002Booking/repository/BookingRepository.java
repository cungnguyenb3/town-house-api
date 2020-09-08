package vn.com.pn.screen.f002Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.pn.screen.f002Booking.entity.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT * FROM bookings WHERE user_id = ?1", nativeQuery = true)
    List<Booking> getListBookingHistory(Long userId);

    @Query(value = "SELECT * FROM bookings INNER JOIN hosts ON hosts.id = bookings.host_id" +
                " INNER JOIN users ON hosts.agent_id = users.id" +
            " WHERE DATE(bookings.check_in_date) = CURRENT_DATE OR DATE(bookings.check_in_date) > CURRENT_DATE" +
            " OR DATE(bookings.check_out_date) = CURRENT_DATE OR DATE(bookings.check_out_date) > CURRENT_DATE" +
            " AND hosts.agent_id = ?1", nativeQuery = true)
    List<Booking> getBookingByCurrentDateAndUser(Long userId);

    @Query(value = "SELECT * FROM bookings INNER JOIN hosts ON hosts.id = bookings.host_id" +
            " AND hosts.agent_id = ?1 order by bookings.created_at DESC", nativeQuery = true)
    List<Booking> getBookingByAgentId(Long userId);
}
