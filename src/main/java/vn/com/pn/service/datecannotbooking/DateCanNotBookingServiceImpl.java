package vn.com.pn.service.datecannotbooking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.dto.DateCanNotBookingDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.DateCanNotBooking;
import vn.com.pn.repository.datecannotbooking.DateCanNotBookingRepository;

@Service
public class DateCanNotBookingServiceImpl implements DateCanNotBookingService{
    @Autowired
    private DateCanNotBookingRepository dateCanNotBookingRepository;

    public BaseOutput insert(DateCanNotBookingDTO dateCanNotBookingDTO){
        try {
            DateCanNotBooking dateCanNotBooking = new DateCanNotBooking();
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
