package vn.com.pn.screen.f002Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.com.pn.utils.DateAuditUtil;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m002Host.entity.Host;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "bookings")
public class Booking extends DateAuditUtil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "host_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Host host;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private String bookingCode;

    private Date checkInDate;

    private Date checkOutDate;

    private Long pricePerNight;

    private Integer nights;

    private Long roomPrice;

    private Long cleanCosts;

    private Long serviceCharge;

    private Long totalPrice;

    private Integer guests;

    private Integer numberOfAdultGuest;

    private Integer numberOfChildrenGuest;

    private Integer numberOfInfantGuest;

    private boolean isAcceptedFromHost;

    private boolean status;

    private boolean isPaid;

    @Column(name = "is_cancel")
    private boolean isCancel;
}
