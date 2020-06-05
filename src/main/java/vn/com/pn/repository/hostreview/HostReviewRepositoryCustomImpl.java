package vn.com.pn.repository.hostreview;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vn.com.pn.common.dto.HostReviewWithUserDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HostReviewRepositoryCustomImpl {
    @Autowired
    private EntityManager entityManager;

    public List<?> getHostReview (Long id) {
        List<HostReviewWithUserDTO> postDTOs = entityManager.createNativeQuery(
                "SELECT new vn.com.pn.common.dto.HostReviewWithUserDTO (hr.content) FROM host_reviews hr " +
                        "INNER JOIN bookings bk on hr.booking_id = bk.id " +
                        "INNER JOIN users ON bk.user_id = users.id " +
                        "INNER JOIN hosts ON bk.host_id = hosts.id " +
                        "WHERE hosts.id = :id")
                .setParameter( "id",  id)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer( Transformers.aliasToBean( HostReviewWithUserDTO.class ) )
                .getResultList();

        return postDTOs;
    }

}
