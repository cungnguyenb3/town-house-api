package vn.com.pn.screen.m002Host.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.screen.m002Host.dto.DateCanNotBookingDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m002Host.entity.DateCanNotBooking;
import vn.com.pn.screen.m002Host.repository.DateCanNotBookingRepository;

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
