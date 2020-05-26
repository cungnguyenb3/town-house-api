package vn.com.pn.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="bookings")
public class Booking extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "host_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Host host;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
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
}
