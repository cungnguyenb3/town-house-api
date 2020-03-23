package vn.com.pn.service.booking;

import vn.com.pn.common.dto.BookingCancelDTO;
import vn.com.pn.common.dto.BookingDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.User;

public interface BookingService {
    BaseOutput getAll();
    BaseOutput insert(BookingDTO bookingDTO, User userLogin);
    BaseOutput bookingCancel(BookingCancelDTO bookingCancelDTO, User userLogin);
}
