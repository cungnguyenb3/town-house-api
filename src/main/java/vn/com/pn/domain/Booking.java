package vn.com.pn.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="bookings")
public class Booking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "host_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Host host;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "check_in_date", nullable = false)
    private Date checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private Date checkOutDate;

    @Column(name = "price_per_day", nullable = false)
    private BigDecimal pricePerDay;

    @Column(name = "price_for_stay", nullable = false)
    private BigDecimal priceForStay;

    @Column(name = "tax_paid", nullable = false)
    private BigDecimal taxPaid;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    @Column(name = "cancel_date")
    private Date cancelDate;

    @Column(name = "is_refund")
    private boolean isRefund;

    @Column(name = "refund_paid")
    private BigDecimal refundPaid;

    @Column(name = "effective_amount")
    private BigDecimal effectiveAmount;

    @Column(name = "booking_date")
    private Date bookingDate;

    private boolean status;

    private String note;
}
