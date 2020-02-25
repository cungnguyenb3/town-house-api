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

//
//    @Column(name = "address", nullable = false)
//    private String address;
//
//    @Column(name = "latitude", nullable = false)
//    private String latitude;
//
//    @Column(name = "logitude", nullable = false)
//    private String logitude;
//
//    @Column(name = "bedroom_count", nullable = false)
//    private String bedroomCount;
//
//    @Column(name = "bed", nullable = false)
//    private String bed;
//
//    @Column(name = "bathroom_count", nullable = false)
//    private String bathroomCount;
//
//    @Column(name = "availability_type", nullable = false)
//    private String availabilityType;
//
//    @Column(name = "start_date", nullable = false)
//    private String startDate;
//
//    @Column(name = "end_date", nullable = false)
//    private String endDate;
//
//    @Column(name = "price", nullable = false)
//    private String price;
//
//    @Column(name = "price_type", nullable = false)
//    private String priceType;
//
//    @Column(name = "minimum_stay", nullable = false)
//    private String priceType;
//
//    @Column(name = "minimum_stay_type", nullable = false)
//    private String minimumStayType;
//
//    @Column(name = "refund_type", nullable = false)
//    private String refundType;
//
//    @Column(name = "status", nullable = false)
//    private String status;
}
