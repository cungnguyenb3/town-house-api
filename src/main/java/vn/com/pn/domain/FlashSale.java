package vn.com.pn.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FlashSale {
    private Long id;
    private String code;
    private LocalDateTime startDay;
    private LocalDateTime expirationDate;
    private Short discountPercent;
    private Long discountMoney;
    private boolean isDiscountWithPercent;
}
