package vn.com.pn.screen.f003Review.repository;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vn.com.pn.screen.f003Review.dto.HostReviewWithUserDTO;

import javax.persistence.*;
import java.util.List;

@Repository
public class HostReviewRepositoryCustomImpl {
    @Autowired
    private EntityManager entityManager;

    public List<?> getHostReview(Long hostId) {
        List<HostReviewWithUserDTO> postDTOs = entityManager.createQuery(
                "SELECT NEW vn.com.pn.screen.f003Review.dto.HostReviewWithUserDTO (hr.id, us.fullName, hr.content, hr.starRating, hr.createdAt) FROM HostReview hr " +
                        "INNER JOIN Booking bk ON bk.id = hr.booking.id " +
                        "INNER JOIN Host ht ON bk.host.id = ht.id " +
                        "INNER JOIN User us ON bk.user.id = us.id " +
                        "WHERE ht.id = :id", HostReviewWithUserDTO.class)
                .setParameter("id", hostId)
                .getResultList();
        return postDTOs;
    }

}
