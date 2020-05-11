package vn.com.pn.service.booking;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.BookingCalculatePriceDTO;
import vn.com.pn.common.dto.BookingCancelDTO;
import vn.com.pn.common.dto.BookingDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.Booking;
import vn.com.pn.domain.CalculatePriceResult;
import vn.com.pn.domain.Host;
import vn.com.pn.domain.User;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.repository.booking.BookingRepository;
import vn.com.pn.repository.host.HostRepository;
import vn.com.pn.service.mail.MailService;
import vn.com.pn.utils.RandomStringUtil;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BookingServiceImpl implements BookingService {
    private static Log logger = LogFactory.getLog(BookingServiceImpl.class);

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private BookingRepository bookingRepository;


    public BaseOutput calculatePrice(BookingCalculatePriceDTO bookingCalculatePriceDTO) {
        CalculatePriceResult calculatePriceResult = new CalculatePriceResult();

        LocalDate startDate = LocalDate.parse(bookingCalculatePriceDTO.getStartDate());
        calculatePriceResult.setStartDate(startDate.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        LocalDate endDate = LocalDate.parse(bookingCalculatePriceDTO.getEndDate());
        calculatePriceResult.setEndDate(endDate.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        long nights = Days.daysBetween(startDate, endDate).getDays();
        calculatePriceResult.setNights(nights);

        LocalDate currentDate = LocalDate.now();
        if (startDate.isAfter(currentDate) || startDate.isEqual(currentDate)) {
            if (endDate.isAfter(startDate)) {
                if (bookingCalculatePriceDTO.getHostId() != null && bookingCalculatePriceDTO.getHostId() != "") {
                    Host host = hostRepository.findById(Integer.parseInt(bookingCalculatePriceDTO.getHostId())).orElseThrow(()
                            -> new ResourceNotFoundException("User", "id", bookingCalculatePriceDTO.getHostId()));
                    if (host != null) {
                        if (host.isAddChildrenAndInfantIntoMaximumGuest() && host.getNumberOfInfantGuest() != 0){
                            calculatePriceResult.setNumberOfInfantGuest(Integer.parseInt(bookingCalculatePriceDTO.getNumberOfInfantGuest()));
                        }else {
                            calculatePriceResult.setNumberOfInfantGuest(0);
                        }
                        int guests = Integer.parseInt(bookingCalculatePriceDTO.getNumberOfAdultGuest())
                                + Integer.parseInt(bookingCalculatePriceDTO.getNumberOfChildrenGuest());
                        calculatePriceResult.setNumberOfAdultGuest(Integer.parseInt(bookingCalculatePriceDTO.getNumberOfAdultGuest()));
                        calculatePriceResult.setNumberOfChildrenGuest(Integer.parseInt(bookingCalculatePriceDTO.getNumberOfChildrenGuest()));
                        calculatePriceResult.setGuests(guests);
                        if (host.getNumberOfMaximumGuest() >= guests) {
                            long cleanCosts = host.getCleaningCosts();
                            calculatePriceResult.setPricePerNight(host.getStandardPriceMondayToThursday());
                            calculatePriceResult.setCleanCosts(cleanCosts);
                            long priceBeforeCleanCosts = 0;
                            long serviceChargeTotal = 0;
                            long price = 0;
                            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                                if (date.getDayOfWeek() == 5 || date.getDayOfWeek() == 6 || date.getDayOfWeek() == 7) {
                                    priceBeforeCleanCosts += host.getStandardPriceFridayToSunday();
                                } else {
                                    priceBeforeCleanCosts += host.getStandardPriceMondayToThursday();
                                }
                            }
                            calculatePriceResult.setPriceWithoutCleanCostsAndServiceCharge(priceBeforeCleanCosts);
                            if (host.getServiceChargePercent() == 0) {
                                price = priceBeforeCleanCosts + cleanCosts;
                                calculatePriceResult.setServiceCharge(0l);
                                calculatePriceResult.setTotalPrice(price);
                                return CommonFunction.successOutput(calculatePriceResult);
                            }
                            if (host.getServiceChargePercent() != 0) {
                                serviceChargeTotal = (priceBeforeCleanCosts + cleanCosts) * (host.getServiceChargePercent() / 100);
                                price = priceBeforeCleanCosts + cleanCosts + serviceChargeTotal;
                                calculatePriceResult.setServiceCharge(serviceChargeTotal);
                                calculatePriceResult.setTotalPrice(price);
                                return CommonFunction.successOutput(calculatePriceResult);
                            }
                        }
                    }
                }
            }
        }
        return CommonFunction.failureOutput();
    }

    @Override
    public BaseOutput getAll() {
        logger.info("BookingServiceImpl.getAll");
        List<Object> listBooking = new ArrayList<>(bookingRepository.findAll());
        return CommonFunction.successOutput(listBooking);
    }

    @Override
    public BaseOutput insert(BookingDTO bookingDTO, User userLogin) {
        logger.info("BookingServiceImpl.insert");
        try {
            Booking booking = getInsertBookingInfo(bookingDTO, userLogin);
            sendEmailRequestForUser(booking);
            sendEmailRequestForHostAgent(booking);
            return CommonFunction.successOutput(bookingRepository.save(booking));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    private Booking getInsertBookingInfo(BookingDTO bookingDTO, User userLogin) {
        logger.info("BookingServiceImpl.getInsertBookingInfo");
        Booking booking = new Booking();
        if (bookingDTO.getHostId() != null && bookingDTO.getHostId() != "") {
            Host host = hostRepository.findById(Integer.parseInt(bookingDTO.getHostId())).orElse(null);
            if (host == null) {
                throw new ResourceNotFoundException("Host", "id", bookingDTO.getHostId());
            }
            booking.setHost(host);
        }
        if (userLogin != null) {
            booking.setUser(userLogin);
        }
        if (bookingDTO.getCheckInDate() != null && bookingDTO.getCheckInDate() != "") {
            booking.setCheckInDate(CommonFunction.convertStringToDateObject(bookingDTO.getCheckInDate()));
        }
        if (bookingDTO.getCheckOutDate() != null && bookingDTO.getCheckOutDate() != "") {
            booking.setCheckOutDate(CommonFunction.convertStringToDateObject(bookingDTO.getCheckOutDate()));
        }
        if (bookingDTO.getPricePerNight() != null && bookingDTO.getPricePerNight() != "") {
            booking.setPricePerNight(new Long(bookingDTO.getPricePerNight()));
        }
        if (bookingDTO.getCleanCosts() != null && bookingDTO.getCleanCosts() != "") {
            booking.setCleanCosts(new Long(bookingDTO.getCleanCosts()));
        }
        if (bookingDTO.getServiceCharge() != null && bookingDTO.getServiceCharge() != "") {
            booking.setServiceCharge(new Long(bookingDTO.getServiceCharge()));
        }
        if (bookingDTO.getTotalPrice() != null && bookingDTO.getTotalPrice() != "") {
            booking.setTotalPrice(new Long(bookingDTO.getTotalPrice()));
        }
        if (bookingDTO.getRoomPrice() != null && bookingDTO.getRoomPrice() != "") {
            booking.setRoomPrice(new Long(bookingDTO.getRoomPrice()));
        }
        if (bookingDTO.getNights() != null && bookingDTO.getNights() != "") {
            booking.setNights(Integer.parseInt(bookingDTO.getNights()));
        }
        if (bookingDTO.getGuests() != null && bookingDTO.getGuests() != "") {
            booking.setGuests(Integer.parseInt(bookingDTO.getGuests()));
        }
        if (bookingDTO.getNumberOfAdultGuest() != null && bookingDTO.getNumberOfAdultGuest() != "") {
            booking.setNumberOfAdultGuest(Integer.parseInt(bookingDTO.getNumberOfAdultGuest()));
        }
        if (bookingDTO.getNumberOfChildrenGuest() != null && bookingDTO.getNumberOfChildrenGuest() != "") {
            booking.setNumberOfChildrenGuest(Integer.parseInt(bookingDTO.getNumberOfChildrenGuest()));
        }
        if (bookingDTO.getNumberOfInfantGuest() != null && bookingDTO.getNumberOfInfantGuest() != "") {
            booking.setNumberOfInfantGuest(Integer.parseInt(bookingDTO.getNumberOfInfantGuest()));
        }
        RandomStringUtil randomStringUtil = new RandomStringUtil(6);
        booking.setBookingCode(randomStringUtil.nextString());
        booking.setAcceptedFromHost(false);
        booking.setPaid(false);
        booking.setStatus(true);
        return booking;
    }

    @Override
    public BaseOutput confirmBookingRequest(String bookingId, int userId) {
        Booking booking = bookingRepository.findById(Integer.parseInt(bookingId))
                .orElse(null);
        if (booking != null) {
            if(userId == booking.getHost().getUser().getId()){
                booking.setAcceptedFromHost(true);
                sendEmailBookingConfirmForUser(booking);
                return CommonFunction.successOutput(bookingRepository.save(booking));
            }
        }
        return CommonFunction.failureOutput();
    }

    @Override
    public BaseOutput bookingCancel(BookingCancelDTO bookingCancelDTO, User userLogin) {
        logger.info("BookingServiceImpl.bookingCancel");
//        try {
//            Booking booking = bookingRepository.findById(Integer.parseInt(bookingCancelDTO.getId())).orElseThrow(()
//                    -> new ResourceNotFoundException("User", "id", bookingCancelDTO.getId()));
//            if (bookingCancelDTO.getStatus() != null && bookingCancelDTO.getStatus() != "") {
//                if (userLogin != null) {
//                    booking.setUser(userLogin);
//                }
//                if (bookingCancelDTO.getStatus().equals("0")) {
//                    booking.setStatus(false);
//                    return CommonFunction.successOutput(bookingRepository.save(booking));
//                } else {
//                    return CommonFunction.errorLogic(400, "Status don't correct!");
//                }
//            }
//        } catch (Exception e) {
//            logger.error(ScreenMessageConstants.FAILURE, e);
//            return CommonFunction.failureOutput();
//        }
        return null;
    }

    private void sendEmailRequestForUser(Booking booking) {
        try {
            String emailSubject = "Bạn đã gửi yêu cầu đặt phòng tại " + booking.getHost().getName();
            StringBuilder emailContent = new StringBuilder();

            String greeting = "Xin chào " + booking.getUser().getFullName();
            String introduction = "\n\nBạn đã yêu cầu đặt phòng qua hệ thống của Town House";
            String propertyName = "\n\nTên nhà: " + booking.getHost().getName();
            String bookingCode = "\nMã đặt phòng: " + booking.getBookingCode();

            String bookingDate = "\nNgày đặt phòng: " + LocalDate.now();
            String cross = "\n------------------------------------------------------------------------------------------";
            String generalInformation = "\nThông tin khách hàng ";
            String fullName = "\nHọ và tên: " + booking.getUser().getFullName();
            String email = "\nEmail: " + booking.getUser().getEmail();
            String phone = "";
            if (booking.getUser().getPhone() != null) {
                phone = "\nPhone: " + booking.getUser().getPhone() ;
            }

            Date checkInDateTime = booking.getCheckInDate();
            java.time.LocalDate checkInDate = checkInDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Date checkOutDateTime = booking.getCheckOutDate();
            java.time.LocalDate checkOutDate = checkOutDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            String reservationDates = "\nKhoảng thời gian đặt phòng: " + checkInDate + " cho đến " + checkOutDate;
            String numberOfNights = "\nSố đêm đặt phòng: " + booking.getNights();
            String numberOfGuests;
            if (booking.getNumberOfInfantGuest() == 0) {
                if(booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn";
                }
                else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em";
                }
            }
            else {
                if(booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn và "
                        + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                }
                else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                    + booking.getNumberOfChildrenGuest() + " trẻ em" + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                }
            }
            String pricePerNight = "\nGiá cho mỗi đêm: "  + booking.getPricePerNight();
            String totalPrice = "\nTổng tiền: " + booking.getTotalPrice() + " VMĐ";
            String noticed = "\n\nYêu cầu đặt phòng của bạn đang được xử lý. Trong vòng 24 giờ tới, chúng tôi sẽ " +
                    "thông báo cho bạn yêu đặt phòng của bạn đã được chấp nhận hay không.\n\n";
            String thanks = "Cảm ơn bạn dã sử dụng dịch vụ của Town Hose.\n\n";
            String sign = "Trân trọng, \nTown house team";

            emailContent.append(greeting).append(introduction).append(propertyName).append(bookingCode).append(bookingDate)
                    .append(cross).append(generalInformation).append(fullName).append(email).append(phone).append(cross)
                    .append(reservationDates).append(numberOfNights).append(numberOfGuests).append(pricePerNight).append(cross)
                    .append(totalPrice).append(cross).append(noticed).append(thanks).append(sign);
            mailService.sendEmail(booking.getUser().getEmail(), emailSubject, emailContent);
        } catch (MailException mailException) {
            logger.error(ScreenMessageConstants.FAILURE, mailException);
        }
    }

    private void sendEmailRequestForHostAgent(Booking booking) {
        try {
            String emailSubject = "Xác nhận yêu cầu đặt phòng " + booking.getHost().getName();
            StringBuilder emailContent = new StringBuilder();

            String greeting = "Xin chào " + booking.getHost().getUser().getFullName();
            String introduction = "\n\nBạn đã nhận được yêu cầu đặt phòng từ " + booking.getUser().getFullName();
            String propertyName = "\n\nTên nhà: " + booking.getHost().getName();
            String bookingCode = "\nMã đặt phòng: " + booking.getBookingCode();

            String bookingDate = "\nNgày đặt phòng: " + LocalDate.now();
            String cross = "\n------------------------------------------------------------------------------------------";
            String generalInformation = "\nThông tin khách hàng ";
            String fullName = "\nHọ và tên: " + booking.getUser().getFullName();
            String email = "\nEmail: " + booking.getUser().getEmail();
            String phone = "";
            if (booking.getUser().getPhone() != null) {
                phone = "\nPhone: " + booking.getUser().getPhone() ;
            }

            Date checkInDateTime = booking.getCheckInDate();
            java.time.LocalDate checkInDate = checkInDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Date checkOutDateTime = booking.getCheckOutDate();
            java.time.LocalDate checkOutDate = checkOutDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            String reservationDates = "\nKhoảng thời gian đặt phòng: " + checkInDate + " cho đến " + checkOutDate;
            String numberOfNights = "\nSố đêm đặt phòng: " + booking.getNights();
            String numberOfGuests;
            if (booking.getNumberOfInfantGuest() == 0) {
                if(booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn";
                }
                else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em";
                }
            }
            else {
                if(booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn và "
                            + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                }
                else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em" + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                }
            }
            String pricePerNight = "\nGiá cho mỗi đêm: "  + booking.getPricePerNight();
            String totalPrice = "\nTổng tiền: " + booking.getTotalPrice() + " VMĐ";
            String noticed = "\n\nNếu bạn chấp nhận yêu cầu đặt phòng. Trong vòng 24 giờ tới," +
                    "vui lòng truy cập vào ứng dụng Townhouse host và xác nhận cho thuê phòng.\n\n";
            String thanks = "Cảm ơn bạn dã sử dụng dịch vụ của Town Hose.\n\n";
            String sign = "Trân trọng, \nTown house team";

            emailContent.append(greeting).append(introduction).append(propertyName).append(bookingCode).append(bookingDate)
                    .append(cross).append(generalInformation).append(fullName).append(email).append(phone).append(cross)
                    .append(reservationDates).append(numberOfNights).append(numberOfGuests).append(pricePerNight)
                    .append(cross).append(totalPrice).append(cross).append(noticed).append(thanks).append(sign);
            mailService.sendEmail(booking.getHost().getUser().getEmail(), emailSubject, emailContent);
        } catch (MailException mailException) {
            logger.error(ScreenMessageConstants.FAILURE, mailException);
        }
    }

    private void sendEmailBookingConfirmForUser(Booking booking) {
        try {
            String emailSubject = "Xác nhận yêu cầu đặt phòng " + booking.getHost().getName();
            StringBuilder emailContent = new StringBuilder();

            String greeting = "Xin chào " + booking.getHost().getUser().getFullName();
            String introduction = "\n\nXin chúc mừng, yêu cầu đăt phòng của bạn đã được chấp thuận.";
            String propertyName = "\n\nTên nhà: " + booking.getHost().getName();
            String bookingCode = "\nMã đặt phòng: " + booking.getBookingCode();

            String bookingDate = "\nNgày đặt phòng: " + LocalDate.now();
            String cross = "\n------------------------------------------------------------------------------------------";
            String generalInformation = "\nThông tin khách hàng ";
            String fullName = "\nHọ và tên: " + booking.getUser().getFullName();
            String email = "\nEmail: " + booking.getUser().getEmail();
            String phone = "";
            if (booking.getUser().getPhone() != null) {
                phone = "\nPhone: " + booking.getUser().getPhone() ;
            }

            Date checkInDateTime = booking.getCheckInDate();
            java.time.LocalDate checkInDate = checkInDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Date checkOutDateTime = booking.getCheckOutDate();
            java.time.LocalDate checkOutDate = checkOutDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            String reservationDates = "\nKhoảng thời gian đặt phòng: " + checkInDate + " cho đến " + checkOutDate;
            String numberOfNights = "\nSố đêm đặt phòng: " + booking.getNights();
            String numberOfGuests;
            if (booking.getNumberOfInfantGuest() == 0) {
                if(booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn";
                }
                else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em";
                }
            }
            else {
                if(booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn và "
                            + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                }
                else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em" + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                }
            }
            String pricePerNight = "\nGiá cho mỗi đêm: "  + booking.getPricePerNight();
            String totalPrice = "\nTổng tiền: " + booking.getTotalPrice() + " VMĐ";
            String noticed = "\n\nXin vui lòng chuyển tiền vào hệ thống để hoàn tất quá trình đặt phòng";
            String bankingInfo = "\nNgân hàng: Vietcomback - chi nhánh Đà Nẵng";
            String bankingNumber = "\nSố tài khoản: 004100031XXXX";
            String bankingName = "\nTên tài khoản: Công ty cổ phần Town house Việt Nam";
            String bankingContent = "\nNội dung chuyển khoản: TTDP " + booking.getBookingCode();
            String bankingMoney = "\nSố tiền: " + booking.getTotalPrice() + "\nVui lòng chuyển khoản chính xác đến chữ số hàng nghìn.";

            String note = "\n\nSau 24 giờ kể từ email này được gửi đi, nếu bạn không hoàn tất " +
                    "việc thanh toán. Townhouse sẽ hủy yêu cầu đặt phòng của bạn.";

            String thanks = "\n\nCảm ơn bạn dã sử dụng dịch vụ của Town Hose.\n\n";
            String sign = "Trân trọng, \nTown house team";

            emailContent.append(greeting).append(introduction).append(propertyName).append(bookingCode).append(bookingDate)
                    .append(cross).append(generalInformation).append(fullName).append(email).append(phone).append(cross)
                    .append(reservationDates).append(numberOfNights).append(numberOfGuests).append(pricePerNight).append(cross)
                    .append(totalPrice).append(cross).append(noticed).append(bankingInfo).append(bankingNumber).append(bankingName)
                    .append(bankingContent).append(bankingMoney).append(note).append(thanks).append(sign);
            mailService.sendEmail(booking.getUser().getEmail(), emailSubject, emailContent);
        } catch (MailException mailException) {
            logger.error(ScreenMessageConstants.FAILURE, mailException);
        }
    }
}
