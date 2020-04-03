package vn.com.pn.repository.hostreview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.pn.domain.HostReview;

import java.util.ArrayList;
import java.util.List;

public interface HostReviewRepository extends JpaRepository <HostReview, Integer> {
    @Query(value="SELECT star_rating  FROM host_reviews WHERE host_id = ?1", nativeQuery=true)
    List<Integer> findStarRatingByHostId(int hostId);
}
