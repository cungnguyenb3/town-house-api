package vn.com.pn.screen.f003Review.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.com.pn.screen.f002Booking.entity.Booking;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="host_reviews")
public class HostReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Booking booking;

    @Lob
    private String content;

    @Column(name = "star_rating")
    private Integer starRating;

    @Column(name = "is_delete", nullable = true)
    private boolean isDelete;

    @Column(name = "is_outstanding", nullable = true)
    private boolean isOutstanding;
}

