package vn.com.pn.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name="hosts")
@EntityListeners(AuditingEntityListener.class)
public class Host implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String name;

    @NonNull
    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostAgent hostAgent;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostCategory hostCategory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "host_room_type_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostRoomType hostRoomType;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostCity hostCity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cancellation_policy_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostCancellationPolicy hostCancellationPolicy;

    private String address;

    private String latitude;

    private String longitude;

    @Column(name = "bedroom_count")
    private Integer bedroomCount;

    private Integer bed;

    @Column(name = "bathroom_count")
    private Integer bathroomCount;

    @Column(name = "availability_type")
    private boolean availabilityType;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    private BigDecimal price;

    @Column(name = "price_type")
    private boolean priceType;

    @Column(name = "minimum_stay")
    private String minimumStay;

    @Column(name = "minimum_stay_type")
    private boolean minimumStayType;

    @Column(name = "refund_type")
    private boolean refundType;

    private boolean status;

    private float star;

    @Column(name = "total_review")
    private Integer totalReview;
}
