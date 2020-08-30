package vn.com.pn.screen.f002Booking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingUserResponseDTO {
    private BookingUserDTO user;
    private BookingRoomDTO room;
    private int night;
    private int guests;
}
