package vn.com.pn.screen.m002Host.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.pn.validate.anotation.Date;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DateCanNotBookingResDTO {
    List<DateResDTO> dates;
//    List<LocalDate> dates;
}

