package vn.com.pn.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.domain.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
