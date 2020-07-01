package vn.com.pn.screen.f003Review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.pn.screen.f003Review.entity.HostReview;

import java.util.List;

@Repository
public interface HostReviewRepository extends JpaRepository<HostReview, Long> {

    @Query(value = "SELECT host_reviews.star_rating FROM host_reviews " +
            "INNER JOIN bookings on host_reviews.booking_id = bookings.id " +
            "INNER JOIN hosts ON bookings.host_id = hosts.id WHERE hosts.id = ?1", nativeQuery = true)
    List<Integer> findStarRatingByHostId(long hostId);

//    @Query("SELECT new vn.com.pn.screen.f003Review.dto.HostReviewWithUserDTO(hr.content) FROM HostReview hr \n" +
//            "INNER JOIN Booking bk on hr.booking.id = bk.id\n" +
//            "INNER JOIN User u ON bk.id = User.id\n" +
//            "INNER JOIN Host ON bk.host.id = Host.id \n" +
//            "WHERE Host.id = ?1")
//    List<Object> findHostReviewByHostId(long hostId);

}
