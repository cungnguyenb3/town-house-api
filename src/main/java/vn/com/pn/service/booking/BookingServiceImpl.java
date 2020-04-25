package vn.com.pn.service.booking;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.BookingCancelDTO;
import vn.com.pn.common.dto.BookingDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.Booking;
import vn.com.pn.domain.Host;
import vn.com.pn.domain.User;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.repository.booking.BookingRepository;
import vn.com.pn.repository.host.HostRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private static Log logger = LogFactory.getLog(BookingServiceImpl.class);

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("BookingServiceImpl.getAll");
        List<Object> listBooking = new ArrayList<>(bookingRepository.findAll());
        return  CommonFunction.successOutput(listBooking);
    }

    @Override
    public BaseOutput insert(BookingDTO bookingDTO, User userLogin) {
        logger.info("BookingServiceImpl.insert");
        try {
            Booking booking = getInsertBookingInfo(bookingDTO, userLogin);
            return CommonFunction.successOutput(bookingRepository.save(booking));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    private Booking getInsertBookingInfo(BookingDTO bookingDTO, User userLogin) {
        logger.info("BookingServiceImpl.getInsertBookingInfo");
        Booking booking = new Booking();
        if (bookingDTO.getHostId() != null && bookingDTO.getHostId() != ""){
            Host host = hostRepository.findById(Integer.parseInt(bookingDTO.getHostId())).orElse(null);
            if (host == null) {
                throw new ResourceNotFoundException("Host", "id",bookingDTO.getHostId());
            }
            booking.setHost(host);
        }
        if (userLogin != null) {
            booking.setUser(userLogin);
        }
        if (bookingDTO.getCheckInDate() != null && bookingDTO.getCheckInDate() != ""){
            booking.setCheckInDate(CommonFunction.convertStringToDateObject(bookingDTO.getCheckInDate()));
        }
        if (bookingDTO.getCheckOutDate() != null && bookingDTO.getCheckOutDate() != ""){
            booking.setCheckOutDate(CommonFunction.convertStringToDateObject(bookingDTO.getCheckOutDate()));
        }
        if (bookingDTO.getPricePerDay() != null && bookingDTO.getPricePerDay() != ""){
            booking.setPricePerDay(new BigDecimal(bookingDTO.getPricePerDay()));
        }
        if (bookingDTO.getPriceForStay() != null && bookingDTO.getPriceForStay() != ""){
            booking.setPriceForStay(new BigDecimal(bookingDTO.getPriceForStay()));
        }
        if (bookingDTO.getTaxPaid() != null && bookingDTO.getTaxPaid() != ""){
            booking.setTaxPaid(new BigDecimal(bookingDTO.getTaxPaid()));
        }
        if (bookingDTO.getAmountPaid() != null && bookingDTO.getAmountPaid() != ""){
            booking.setAmountPaid(new BigDecimal(bookingDTO.getAmountPaid()));
        }
        if (bookingDTO.getCancelDate() != null && bookingDTO.getCancelDate() != ""){
            booking.setCancelDate(CommonFunction.convertStringToDateObject(bookingDTO.getCancelDate()));
        }
        if (bookingDTO.getIsRefund() != null && bookingDTO.getIsRefund() != ""){
            if(bookingDTO.getIsRefund().equals("0")){
                booking.setRefund(false);
            }
            if(bookingDTO.getIsRefund().equals("1")){
                booking.setRefund(true);
            }
        }
        if (bookingDTO.getRefundPaid() != null && bookingDTO.getRefundPaid() != ""){
            booking.setRefundPaid(new BigDecimal(bookingDTO.getRefundPaid()));
        }
        if (bookingDTO.getEffectiveAmount() != null && bookingDTO.getEffectiveAmount() != ""){
            booking.setEffectiveAmount(new BigDecimal(bookingDTO.getEffectiveAmount()));
        }
        if (bookingDTO.getBookingDate() != null && bookingDTO.getBookingDate() != ""){
            booking.setBookingDate(CommonFunction.convertStringToDateObject(bookingDTO.getBookingDate()));
        }
        if (bookingDTO.getStatus() != null && bookingDTO.getStatus() != ""){
            if(bookingDTO.getStatus().equals("0")){
                booking.setStatus(false);
            }
            if(bookingDTO.getStatus().equals("1")){
                booking.setStatus(true);
            }
        }
        if (bookingDTO.getNote() != null && bookingDTO.getNote() != ""){
            booking.setNote(bookingDTO.getNote());
        }
        return booking;
    }

    @Override
    public BaseOutput bookingCancel(BookingCancelDTO bookingCancelDTO, User userLogin){
        logger.info("BookingServiceImpl.bookingCancel");
        try {
            Booking booking = bookingRepository.findById(Integer.parseInt(bookingCancelDTO.getId())).orElseThrow(()
                    -> new  ResourceNotFoundException("User", "id", bookingCancelDTO.getId()));
            if (bookingCancelDTO.getStatus() != null && bookingCancelDTO.getStatus() != "") {
                if (userLogin != null) {
                    booking.setUser(userLogin);
                }
                if(bookingCancelDTO.getStatus().equals("0")){
                    booking.setStatus(false);
                    return CommonFunction.successOutput(bookingRepository.save(booking));
                } else {
                    return CommonFunction.errorLogic(400,"Status don't correct!");
                }
            }
        } catch(Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
        return null;
    }


}
