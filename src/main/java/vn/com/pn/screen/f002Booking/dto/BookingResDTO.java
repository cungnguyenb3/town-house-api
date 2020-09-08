package vn.com.pn.screen.f002Booking.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m002Host.entity.Host;

@Getter
@Setter
public class BookingResDTO {
    private String id;
    private User user;
    private String createDate;
    private String checkInDate;
    private String checkOutDate;
    private Host host;
    private String numberOfAdultGuest;
    private String numberOfChildrenGuest;
    private String numberOfInfantGuest;
    private boolean isAcceptedFromHost;
    private boolean status;
    private boolean isPaid;
    private boolean isCancel;
    private String totalPrice;
}
