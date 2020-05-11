package vn.com.pn.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "host_discount")
public class HostDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Short discountPercent;
    private Long priceBeforeDiscountMondayToThursday;
    private Long priceBeforeDiscountFridayToSunday;
    private LocalDate startDiscountDay;
    private LocalDate endDiscountDay;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "host_id", referencedColumnName = "id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Host host;
}
