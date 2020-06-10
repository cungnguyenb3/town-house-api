package vn.com.pn.screen.f002Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.screen.f002Booking.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
