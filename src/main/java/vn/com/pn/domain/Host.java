package vn.com.pn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name="hosts")
@EntityListeners(AuditingEntityListener.class)
public class Host implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostAgent hostAgent;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostCategory hostCategory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "host_room_type_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostRoomType hostRoomType;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "city_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HostCity hostCity;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "latitude", nullable = true)
    private String latitude;

    @Column(name = "longitude", nullable = true)
    private String longitude;

    @Column(name = "bedroom_count", nullable = true)
    private int bedroomCount;

    @Column(name = "bed", nullable = true)
    private int bed;

    @Column(name = "bathroom_count", nullable = true)
    private int bathroomCount;

    @Column(name = "availability_type", nullable = true)
    private boolean availabilityType;
//
    @Column(name = "start_date", nullable = true)
    private Date startDate;
//
    @Column(name = "end_date", nullable = true)
    private Date endDate;
//
    @Column(name = "price", nullable = true)
    private BigDecimal price;

    @Column(name = "price_type", nullable = true)
    private boolean priceType;

    @Column(name = "minimum_stay", nullable = true)
    private String minimumStay;

    @Column(name = "minimum_stay_type", nullable = true)
    private boolean minimumStayType;
//
    @Column(name = "refund_type", nullable = true)
    private boolean refundType;

    @Column(name = "status", nullable = true)
    private boolean status;
}
