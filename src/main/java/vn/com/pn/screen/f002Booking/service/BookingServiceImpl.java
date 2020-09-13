package vn.com.pn.screen.f002Booking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.exception.ResourceInvalidInputException;
import vn.com.pn.screen.f002Booking.dto.*;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.config.ScheduledConfig;
import vn.com.pn.screen.f002Booking.entity.Booking;
import vn.com.pn.screen.f002Booking.entity.CalculatePriceResult;
import vn.com.pn.screen.f006Notification.dto.FCMDataRequestDto;
import vn.com.pn.screen.f006Notification.entity.Notification;
import vn.com.pn.screen.f006Notification.repository.NotificationRepository;
import vn.com.pn.screen.f006Notification.service.FCMPushNotificationService;
import vn.com.pn.screen.m001User.entity.UserDeviceToken;
import vn.com.pn.screen.m002Host.entity.Host;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.screen.f002Booking.repository.BookingRepository;
import vn.com.pn.screen.m002Host.repository.HostRepository;
import vn.com.pn.screen.f005Gmail.service.MailService;
import vn.com.pn.utils.MapperUtil;
import vn.com.pn.utils.RandomStringUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {
    private static Log logger = LogFactory.getLog(BookingServiceImpl.class);

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ScheduledConfig scheduledConfig;

    @Autowired
    private FCMPushNotificationService fcmService;

    @Autowired
    private NotificationRepository notificationRepository;

    public BaseOutput calculatePrice(BookingCalculatePriceDTO bookingCalculatePriceDTO) {
        try {
            CalculatePriceResult calculatePriceResult = new CalculatePriceResult();

            LocalDate startDate = LocalDate.parse(bookingCalculatePriceDTO.getStartDate());
            calculatePriceResult.setStartDate(startDate.toString());

            LocalDate endDate = LocalDate.parse(bookingCalculatePriceDTO.getEndDate());
            calculatePriceResult.setEndDate(endDate.toString());

            long nights = Days.daysBetween(startDate, endDate).getDays();
            calculatePriceResult.setNights(nights);

            LocalDate currentDate = LocalDate.now();
            if (endDate.isAfter(startDate)) {
                if (startDate.isAfter(currentDate) || startDate.isEqual(currentDate)) {
                    if (bookingCalculatePriceDTO.getHostId() != null && bookingCalculatePriceDTO.getHostId().trim().length() != 0) {
                        Host host = hostRepository.findById(Long.parseLong(bookingCalculatePriceDTO.getHostId())).orElseThrow(()
                                -> new ResourceNotFoundException("Host", "id", bookingCalculatePriceDTO.getHostId()));
                        if (host != null) {
                            if (host.isAddChildrenAndInfantIntoMaximumGuest() && host.getNumberOfInfantGuest() != 0) {
                                calculatePriceResult.setNumberOfInfantGuest(Integer.parseInt(bookingCalculatePriceDTO.getNumberOfInfantGuest()));
                            } else {
                                calculatePriceResult.setNumberOfInfantGuest(0);
                            }
                            int guests = Integer.parseInt(bookingCalculatePriceDTO.getNumberOfAdultGuest())
                                    + Integer.parseInt(bookingCalculatePriceDTO.getNumberOfChildrenGuest());
                            calculatePriceResult.setNumberOfAdultGuest(Integer.parseInt(bookingCalculatePriceDTO.getNumberOfAdultGuest()));
                            calculatePriceResult.setNumberOfChildrenGuest(Integer.parseInt(bookingCalculatePriceDTO.getNumberOfChildrenGuest()));
                            calculatePriceResult.setHostId(Long.parseLong(bookingCalculatePriceDTO.getHostId()));
                            calculatePriceResult.setGuests(guests);
                            if (guests <= 0) {
                                throw new ResourceInvalidInputException("Số lượng khách không được phép ít hơn 1");
                            }
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
                            } else {
                                throw new ResourceInvalidInputException("Tổng số lượng khác phải ít hơn hoặc bằng với số " +
                                        "lượng khách cho phép tối đa: " + host.getNumberOfMaximumGuest());
                            }
                        }
                    } else {
                        throw new ResourceInvalidInputException("Vui lòng nhập host id");
                    }
                } else {
                    throw new ResourceInvalidInputException("Ngày checkin phải say ngày hiện tại");
                }
            } else {
                throw new ResourceInvalidInputException("Ngày checkin phải trước ngày checkout!");
            }
            throw new Exception();
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException("Dữ liệu đầu vào không chính xác");
        }


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
            pushNotification(booking.getHost().getUser(), "Yêu cầu đặt phòng", "Bạn vừa nhận được yêu cầu đặt phòng "
                    + booking.getHost().getName() + " từ người dùng " + booking.getUser().getFullName() + ".");
            return CommonFunction.successOutput(bookingRepository.save(booking));
        } catch (Exception e) {
            throw new ResourceInvalidInputException("Dữ liệu đầu vào không chính xác!");
        }
    }

    private void pushNotification(User user, String title, String message) throws JsonProcessingException {
        if (user.getDeviceTokens() != null && user.getDeviceTokens().size() != 0) {
            for (UserDeviceToken userDeviceToken : user.getDeviceTokens()) {
                Notification notification = new Notification(title, message, false);
                notification.setUser(user);
                notification.setUpdatedAt(new Date());
                notificationRepository.save(notification);
                fcmService.pushNotification(userDeviceToken.getDeviceToken(), title, message, String.valueOf(notification.getId()));
            }
        }
    }

    private Booking getInsertBookingInfo(BookingDTO bookingDTO, User userLogin) {
        logger.info("BookingServiceImpl.getInsertBookingInfo");
        Booking booking = new Booking();
        if (bookingDTO.getHostId() != null && bookingDTO.getHostId() != "") {
            Host host = hostRepository.findById(Long.parseLong(bookingDTO.getHostId())).orElse(null);
            if (host == null) {
                throw new ResourceNotFoundException("Host", "id", bookingDTO.getHostId());
            }
            booking.setHost(host);
        }
        if (userLogin != null) {
            booking.setUser(userLogin);
        }
        if (bookingDTO.getStartDate() != null && bookingDTO.getStartDate() != "") {
            booking.setCheckInDate(CommonFunction.convertStringToLocalDateObject(bookingDTO.getStartDate()));
        }
        if (bookingDTO.getEndDate() != null && bookingDTO.getEndDate() != "") {
            booking.setCheckOutDate(CommonFunction.convertStringToLocalDateObject(bookingDTO.getEndDate()));
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
        if (bookingDTO.getPriceWithoutCleanCostsAndServiceCharge() != null && bookingDTO.getPriceWithoutCleanCostsAndServiceCharge() != "") {
            booking.setRoomPrice(new Long(bookingDTO.getPriceWithoutCleanCostsAndServiceCharge()));
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
        booking.setStatus(false);
        booking.setCancel(false);
        return booking;
    }

    @Override
    public BaseOutput confirmBookingRequest(String bookingId, long userId) throws JsonProcessingException {
        Booking booking = bookingRepository.findById(Long.parseLong(bookingId))
                .orElse(null);
        System.out.println(booking.getHost().getUser().getId());
        if (booking != null) {
            if (userId == booking.getHost().getUser().getId()) {
                booking.setAcceptedFromHost(true);
                sendEmailBookingConfirmForUser(booking);
                pushNotification(booking.getUser(), "Yêu cầu đã được chấp thuận", "Yêu cầu đặt phòng của bạn đã được " +
                        "chủ nhà chấp thuận. Vui lòng thanh toán để hoàn tất yêu cầu đặt phòng" + ".");
                return CommonFunction.successOutput(bookingRepository.save(booking));
            }
        }
        throw new ResourceNotFoundException("Booking không tìm thấy với id: " + bookingId);
    }

    @Override
    public BaseOutput confirmBookingPaid(String bookingId) throws JsonProcessingException {
        Booking booking = bookingRepository.findById(Long.parseLong(bookingId))
                .orElse(null);
        if (booking != null) {
            booking.setPaid(true);
            sendEmailBookingSuccess(booking);
            setCountdownBookingCompleteTime(booking);
            pushNotification(booking.getUser(),"Thanh toán thành công", "Booking của bạn đã được thanh toán" +
                    " thành công, chúc bạn có những trải nghiệm vui vẻ cùng Town 7.");
            pushNotification(booking.getUser(),"Phòng của bạn đã được thanh toán", "Chúng tôi sẽ chủ động liên " +
                    "hệ để chuyển tiền cho bạn.");
            return CommonFunction.successOutput(bookingRepository.save(booking));
        }
        throw new ResourceNotFoundException("Booking không tìm thấy với id: " + bookingId);
    }

    @Override
    public BaseOutput getBookingById(long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking", "id", id)
        );

        return CommonFunction.successOutput(booking);
    }

    private void sendEmailRequestForUser(Booking booking) {
        try {
            String emailSubject = "Bạn đã gửi yêu cầu đặt phòng tại " + booking.getHost().getName();
            StringBuilder emailContent = new StringBuilder();

            java.time.LocalDate checkInDate = booking.getCheckInDate();

            java.time.LocalDate checkOutDate = booking.getCheckOutDate();

            String numberOfGuests;
            if (booking.getNumberOfInfantGuest() == 0) {
                if (booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn";
                } else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em";
                }
            } else {
                if (booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn và "
                            + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                } else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em" + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                }
            }
            String cleanCost = "";
            if (booking.getCleanCosts() != 0) {
                cleanCost = "\nChi phí dọn dẹp " + booking.getCleanCosts() + "VNĐ";
            }
            String serviceCharge = "";
            if (booking.getServiceCharge() != 0) {
                serviceCharge = "\nPhí dịch vụ: " + booking.getServiceCharge() + "VNĐ";
            }

            emailContent.append("Xin chào " + booking.getUser().getFullName())
                    .append("\n\nBạn đã yêu cầu đặt phòng qua hệ thống của Town House")
                    .append("\n\nTên nhà: " + booking.getHost().getName())
                    .append("\nMã đặt phòng: " + booking.getBookingCode())
                    .append("\nNgày đặt phòng: " + LocalDate.now())
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nThông tin khách hàng ")
                    .append("\nHọ và tên: " + booking.getUser().getFullName())
                    .append("\nEmail: " + booking.getUser().getEmail())
                    .append("\nPhone: " + booking.getUser().getPhone())
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nKhoảng thời gian đặt phòng: Từ ngày " + checkInDate + " cho đến " + checkOutDate)
                    .append("\nSố đêm đặt phòng: " + booking.getNights())
                    .append(numberOfGuests)
                    .append("\nGiá cho mỗi đêm: " + booking.getPricePerNight())
                    .append(cleanCost)
                    .append(serviceCharge)
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nTổng tiền: " + booking.getTotalPrice() + " VMĐ")
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\n\nYêu cầu đặt phòng của bạn đang được xử lý. Trong vòng 24 giờ tới, chúng tôi sẽ " +
                            "thông báo cho bạn yêu đặt phòng của bạn đã được chấp nhận hay không.\n\n")
                    .append("Cảm ơn bạn dã sử dụng dịch vụ của Town Hose.\n\n")
                    .append("Trân trọng, \nTown house team");
            mailService.sendEmail(booking.getUser().getEmail(), emailSubject, emailContent);
        } catch (MailException mailException) {
            logger.error(ScreenMessageConstants.FAILURE, mailException);
        }
    }

    private void sendEmailRequestForHostAgent(Booking booking) {
        try {
            String emailSubject = "Xác nhận yêu cầu đặt phòng " + booking.getHost().getName();
            StringBuilder emailContent = new StringBuilder();

            java.time.LocalDate checkInDate = booking.getCheckInDate();

            java.time.LocalDate checkOutDate = booking.getCheckOutDate();

            String numberOfGuests;
            if (booking.getNumberOfInfantGuest() == 0) {
                if (booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn";
                } else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em";
                }
            } else {
                if (booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn và "
                            + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                } else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em" + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                }
            }

            String cleanCost = null;
            if (booking.getCleanCosts() != 0) {
                cleanCost = "\nChi phí dọn dẹp " + booking.getCleanCosts() + "VNĐ";
            }
            String serviceCharge = null;
            if (booking.getServiceCharge() != 0) {
                serviceCharge = "\nPhí dịch vụ: " + booking.getServiceCharge() + "VNĐ";
            }

            emailContent.append("Xin chào " + booking.getHost().getUser().getFullName())
                    .append("\n\nBạn đã nhận được yêu cầu đặt phòng từ " + booking.getUser().getFullName())
                    .append("\n\nTên nhà: " + booking.getHost().getName())
                    .append("\nMã đặt phòng: " + booking.getBookingCode())
                    .append("\nNgày đặt phòng: " + LocalDate.now())
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nThông tin khách hàng ")
                    .append("\nHọ và tên: " + booking.getUser().getFullName())
                    .append("\nEmail: " + booking.getUser().getEmail())
                    .append("\nPhone: " + booking.getUser().getPhone())
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nKhoảng thời gian đặt phòng: Từ ngày" + checkInDate + " cho đến " + checkOutDate)
                    .append("\nSố đêm đặt phòng: " + booking.getNights())
                    .append(numberOfGuests)
                    .append("\nGiá cho mỗi đêm: " + booking.getPricePerNight())
                    .append(cleanCost)
                    .append(serviceCharge)
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nTổng tiền: " + booking.getTotalPrice() + " VNĐ")
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\n\nNếu bạn chấp nhận yêu cầu đặt phòng. Trong vòng 24 giờ tới," +
                            "vui lòng truy cập vào ứng dụng Townhouse host và xác nhận cho thuê phòng.\n\n")
                    .append("Cảm ơn bạn dã sử dụng dịch vụ của Town Hose.\n\n")
                    .append("Trân trọng, \nTown house team");
            mailService.sendEmail(booking.getHost().getUser().getEmail(), emailSubject, emailContent);
        } catch (MailException mailException) {
            logger.error(ScreenMessageConstants.FAILURE, mailException);
        }
    }

    private void sendEmailBookingConfirmForUser(Booking booking) {
        try {
            String emailSubject = "Yêu cầu đặt phòng đã được chủ nhà chấp thuận " + booking.getHost().getName();
            StringBuilder emailContent = new StringBuilder();

            java.time.LocalDate checkInDate = booking.getCheckInDate();

            java.time.LocalDate checkOutDate = booking.getCheckOutDate();

            String numberOfGuests;
            if (booking.getNumberOfInfantGuest() == 0) {
                if (booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn";
                } else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em";
                }
            } else {
                if (booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn và "
                            + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                } else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em" + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                }
            }

            String cleanCost = "";
            if (booking.getCleanCosts() != 0) {
                cleanCost = "\nChi phí dọn dẹp " + booking.getCleanCosts() + "VNĐ";
            }
            String serviceCharge = "";
            if (booking.getServiceCharge() != 0) {
                serviceCharge = "\nPhí dịch vụ: " + booking.getServiceCharge() + "VNĐ";
            }

            emailContent.append("Xin chào " + booking.getHost().getUser().getFullName())
                    .append("\n\nXin chúc mừng, yêu cầu đăt phòng của bạn đã được chấp thuận.")
                    .append("\n\nTên nhà: " + booking.getHost().getName())
                    .append("\nMã đặt phòng: " + booking.getBookingCode())
                    .append("\nNgày đặt phòng: " + booking.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nThông tin khách hàng ")
                    .append("\nHọ và tên: " + booking.getUser().getFullName())
                    .append("\nEmail: " + booking.getUser().getEmail())
                    .append("\nPhone: " + booking.getUser().getPhone())
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nKhoảng thời gian đặt phòng: Từ ngày" + checkInDate + " cho đến " + checkOutDate)
                    .append("\nSố đêm đặt phòng: " + booking.getNights())
                    .append(numberOfGuests)
                    .append("\nGiá cho mỗi đêm: " + booking.getPricePerNight())
                    .append(cleanCost)
                    .append(serviceCharge)
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nTổng tiền: " + booking.getTotalPrice() + " VMĐ")
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\n\nXin vui lòng chuyển tiền vào hệ thống để hoàn tất quá trình đặt phòng")
                    .append("\nNgân hàng: Vietcomback - chi nhánh Đà Nẵng")
                    .append("\nSố tài khoản: 004100031XXXX")
                    .append("\nTên tài khoản: Công ty cổ phần Town house Việt Nam")
                    .append("\nNội dung chuyển khoản: TTDP " + booking.getBookingCode())
                    .append("\nSố tiền: " + booking.getTotalPrice() + "\nVui lòng chuyển khoản chính xác đến chữ số hàng nghìn.")
                    .append("\n\nSau 24 giờ kể từ email này được gửi đi, nếu bạn không hoàn tất " +
                            "việc thanh toán. Townhouse sẽ hủy yêu cầu đặt phòng của bạn.")
                    .append("\n\nCảm ơn bạn dã sử dụng dịch vụ của Town Hose.\n\n")
                    .append("Trân trọng, \nTown house team");
            mailService.sendEmail(booking.getUser().getEmail(), emailSubject, emailContent);
            setCountdownPaymentTime(booking);
        } catch (MailException mailException) {
            logger.error(ScreenMessageConstants.FAILURE, mailException);
        }
    }

    private void setCountdownPaymentTime(Booking booking) {
        Runnable runnable = () -> checkIsPaidAndSendEmailBookingFailure(booking);

        LocalDateTime date = LocalDateTime.from(LocalDateTime.now().plusDays(1));

        ScheduledTaskRegistrar setUpCronTask = CommonFunction.setUpCronTask(date, runnable);
        scheduledConfig.configureTasks(setUpCronTask);
    }

    private void setCountdownBookingCompleteTime(Booking booking) {
        Runnable runnable = () -> setBookingCompletedAndSendEmailThankful(booking);

        LocalDateTime date = LocalDateTime.of(booking.getCheckOutDate().getYear(), booking.getCheckOutDate().getMonth(),
                booking.getCheckOutDate().getDayOfMonth(), booking.getHost().getCheckOutTime().getHour(),
                booking.getHost().getCheckOutTime().getMinute(),
                booking.getHost().getCheckOutTime().getSecond());
        ScheduledTaskRegistrar setUpCronTask = CommonFunction.setUpCronTask(date, runnable);
        scheduledConfig.configureTasks(setUpCronTask);
    }

    private void checkIsPaidAndSendEmailBookingFailure(Booking booking) {
        if (!booking.isPaid()) {
            try {
                booking.setCancel(true);
                bookingRepository.save(booking);
                pushNotification(booking.getUser(), "Đặt phòng thất bại", "Yêu cầu đặt phòng của bạn đã bị" +
                        "hủy vì không thanh toán trong thời gian quy định");
                pushNotification(booking.getHost().getUser(), "Yêu cầu đặt phòng thất bại", "Yêu cầu đặt phòng" +
                        "đã bị hủy vì khách hàng đã không thanh toán trong thời gian quy định.");
                String emailSubject = "Đặt phòng tại " + booking.getHost().getName() + " thất bại";
                StringBuilder emailContent = new StringBuilder();

                java.time.LocalDate checkInDate = booking.getCheckInDate();

                java.time.LocalDate checkOutDate = booking.getCheckOutDate();

                String numberOfGuests;
                if (booking.getNumberOfInfantGuest() == 0) {
                    if (booking.getNumberOfChildrenGuest() == 0) {
                        numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn";
                    } else {
                        numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                                + booking.getNumberOfChildrenGuest() + " trẻ em";
                    }
                } else {
                    if (booking.getNumberOfChildrenGuest() == 0) {
                        numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn và "
                                + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                    } else {
                        numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                                + booking.getNumberOfChildrenGuest() + " trẻ em" + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                    }
                }

                emailContent.append("Xin chào " + booking.getHost().getUser().getFullName())
                        .append("\n\nYêu cầu đặt phòng của bạn đã hết hạn.")
                        .append("\n\nTên nhà: " + booking.getHost().getName())
                        .append("\nMã đặt phòng: " + booking.getBookingCode())
                        .append("\nNgày đặt phòng: " + booking.getCreatedAt().toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate())
                        .append("\n------------------------------------------------------------------------------------------")
                        .append("\nBooking ID: " + booking.getId())
                        .append("\nKhoảng thời gian đặt phòng: Từ ngày" + checkInDate + " cho đến " + checkOutDate)
                        .append("\nSố đêm đặt phòng: " + booking.getNights())
                        .append(numberOfGuests)
                        .append("\n\nCảm ơn bạn dã sử dụng dịch vụ của Town Hose.\n\n")
                        .append("Trân trọng, \nTown house team");
                mailService.sendEmail(booking.getUser().getEmail(), emailSubject, emailContent);

            } catch (MailException | JsonProcessingException mailException) {
                logger.error(ScreenMessageConstants.FAILURE, mailException);
            }
        }
    }

    private void setBookingCompletedAndSendEmailThankful(Booking booking) {
        try {
            booking.setStatus(true);
            bookingRepository.save(booking);

            String emailSubject = "Cảm ơn bạn đã đặt phòng tại Town House";
            StringBuilder emailContent = new StringBuilder();

            emailContent.append("Xin chào " + booking.getUser().getFullName())
                    .append("\n\nCảm ơn bạn đã sử dụng dịch vụ của chúng tôi để đặt chỗ tại " + booking.getHost().getName() + ".")
                    .append("\n\nChúng tôi rất vui khi xác nhận rằng bạn đã hoành thành quá trình đặt phòng ")
                    .append("trong thời gian " + booking.getNights() + " đêm.")
                    .append("\n\nNếu bạn có bất kỳ phản hồi nào về các dịch vụ của Town House, bạn có thể vui lòng ")
                    .append("cho chúng tôi biết bằng cách trả lời lại email này? Hệ thống giải đáp thắc mắc sẽ trả lời ")
                    .append("cho bạn sớm nhất có thể.")
                    .append("\n\nNếu bạn hài lòng với dịch vụ của chúng tôi, bạn có thể đánh giá chúng tôi và căn hộ bạn vừa ")
                    .append("trải nghiệm 5 để chúng tôi có đổng lục mang đến cho các bạn những trải nghiệm mới mẻ hơn.")
                    .append("\n\nCảm ơn bạn dã sử dụng dịch vụ của Town Hose.\n\n")
                    .append("Trân trọng, \nTown house team");
            mailService.sendEmail(booking.getUser().getEmail(), emailSubject, emailContent);
        } catch (MailException mailException) {
            logger.error(ScreenMessageConstants.FAILURE, mailException);
        }
    }


    private void sendEmailBookingSuccess(Booking booking) {
        try {
            String emailSubject = "Đặt phòng thành công";
            StringBuilder emailContent = new StringBuilder();

            java.time.LocalDate checkInDate = booking.getCheckInDate();

            java.time.LocalDate checkOutDate = booking.getCheckOutDate();

            String numberOfGuests;
            if (booking.getNumberOfInfantGuest() == 0) {
                if (booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn";
                } else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em";
                }
            } else {
                if (booking.getNumberOfChildrenGuest() == 0) {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + " người lớn và "
                            + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                } else {
                    numberOfGuests = "\nSố lượng khách: " + booking.getNumberOfAdultGuest() + "người lớn và "
                            + booking.getNumberOfChildrenGuest() + " trẻ em" + booking.getNumberOfInfantGuest() + " trẻ sơ sinh";
                }
            }

            String cleanCost = "";
            if (booking.getCleanCosts() != 0) {
                cleanCost = "\nChi phí dọn dẹp " + booking.getCleanCosts() + "VNĐ";
            }
            String serviceCharge = "";
            if (booking.getServiceCharge() != 0) {
                serviceCharge = "\nPhí dịch vụ: " + booking.getServiceCharge() + "VNĐ";
            }

            emailContent.append("Xin chào " + booking.getUser().getFullName())
                    .append("\n\nXin chúc mừng, bạn đã hoàn tất quá trình đặt phòng.")
                    .append("\n\nTên nhà: " + booking.getHost().getName())
                    .append("\nTên chủ nhà: " + booking.getHost().getUser().getFullName())
                    .append("\nSố điện thoại của chủ nhà: " + booking.getHost().getUser().getPhone())
                    .append("\nĐịa chỉ: " + booking.getHost().getAddress())
                    .append("\nMã đặt phòng: " + booking.getBookingCode())
                    .append("\nNgày đặt phòng: " + booking.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nThông tin khách hàng ")
                    .append("\nHọ và tên: " + booking.getUser().getFullName())
                    .append("\nEmail: " + booking.getUser().getEmail())
                    .append("\nPhone: " + booking.getUser().getPhone())
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nHướng dẫn nhận phòng: " + booking.getHost().getCheckInInstructions())
                    .append("\nThời gian check in trong vòng từ " + booking.getHost().getEarliestCheckIn()
                            + " đến " + booking.getHost().getCheckOutTime() + ".")
                    .append("\nThời gian check out: " + booking.getHost().getCheckOutTime() + ".")
                    .append("\nHướng dẫn sử dụng tiện ích: "
                            + booking.getHost().getUsingConvenientInstructions() + ".")
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nKhoảng thời gian đặt phòng: Từ ngày" + checkInDate + " cho đến " + checkOutDate)
                    .append("\nSố đêm đặt phòng: " + booking.getNights())
                    .append(numberOfGuests)
                    .append("\nGiá cho mỗi đêm: " + booking.getPricePerNight())
                    .append(cleanCost)
                    .append(serviceCharge)
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\nTổng tiền: " + booking.getTotalPrice() + " VNĐ")
                    .append("\n------------------------------------------------------------------------------------------")
                    .append("\n\nCảm ơn bạn dã sử dụng dịch vụ của Town Hose.\n\n")
                    .append("Trân trọng, \nTown house team");
            mailService.sendEmail(booking.getUser().getEmail(), emailSubject, emailContent);
        } catch (MailException mailException) {
            logger.error(ScreenMessageConstants.FAILURE, mailException);
        }
    }

    public ResponseEntity<?> getBookingByCurrentDateAndUser(Long userId) {
        List<Booking> bookings = bookingRepository.getBookingByCurrentDateAndUser(userId);
        Map<String, Map<String, List<BookingUserResponseDTO>>> bookingResult = new HashMap<>();

        List<String> bookingDates = new ArrayList<>();
        for (Booking booking : bookings) {
            if (!bookingDates.contains(booking.getCheckInDate().toString())) {
                bookingDates.add(booking.getCheckInDate().toString());
            }
            if (!bookingDates.contains(booking.getCheckOutDate().toString())) {
                bookingDates.add(booking.getCheckOutDate().toString());
            }
        }
        for (String bookingDate : bookingDates) {
            Map<String, List<BookingUserResponseDTO>> bookingResultValue = new HashMap<>();
            List<BookingUserResponseDTO> bookingCheckIn = new ArrayList<>();
            for (Booking booking : bookings) {
                if (bookingDate.equals(booking.getCheckInDate().toString())) {
                    BookingUserResponseDTO bookingUserRes = new BookingUserResponseDTO();
                    if (booking.getUser().getFullName() != null && booking.getUser().getEmail() != null){
                        bookingUserRes.setUser(new BookingUserDTO(booking.getUser().getFullName(), booking.getUser().getEmail()));
                    }
                    if (booking.getHost().getName() != null &&
                            booking.getHost().getHostImages().size() != 0) {
                        bookingUserRes.setRoom(new BookingRoomDTO(booking.getHost().getName(),
                                booking.getHost().getHostImages().iterator().next().getWebContentLink()));
                    }
                    if (booking.getGuests() != null) {
                        bookingUserRes.setGuests(booking.getGuests());
                    }
                    if (booking.getNights() != null) {
                        bookingUserRes.setNight(booking.getNights());
                    }
                    bookingCheckIn.add(bookingUserRes);
                }
            }
            if (bookingCheckIn != null && bookingCheckIn.size() != 0) {
                bookingResultValue.put("checkin", bookingCheckIn);
            }

            List<BookingUserResponseDTO> bookingCheckOut = new ArrayList<>();
            for (Booking booking : bookings) {
                if (bookingDate.equals(booking.getCheckOutDate().toString())) {
                    BookingUserResponseDTO bookingUserRes = new BookingUserResponseDTO();
                    if (booking.getUser().getFullName() != null && booking.getUser().getEmail() != null){
                        bookingUserRes.setUser(new BookingUserDTO(booking.getUser().getFullName(), booking.getUser().getEmail()));
                    }
                    if (booking.getHost().getName() != null &&
                            booking.getHost().getHostImages().size() != 0) {
                        bookingUserRes.setRoom(new BookingRoomDTO(booking.getHost().getName(),
                                booking.getHost().getHostImages().iterator().next().getWebContentLink()));
                    }
                    if (booking.getGuests() != null) {
                        bookingUserRes.setGuests(booking.getGuests());
                    }
                    if (booking.getNights() != null) {
                        bookingUserRes.setNight(booking.getNights());
                    }
                    bookingCheckOut.add(bookingUserRes);
                }
            }
            if (bookingCheckOut != null && bookingCheckOut.size() != 0) {
                bookingResultValue.put("checkout", bookingCheckOut);
            }

            bookingResult.put(bookingDate, bookingResultValue);
        }
        return ResponseEntity.ok(bookingResult);
    }

    @Override
    public BaseOutput getAllBookingFromAgent(long userId) {
        List<Booking> bookingList = bookingRepository.getBookingByAgentId(userId);
        List<BookingResDTO> bookingResDTOS = new ArrayList<>();
        for (Booking booking : bookingList) {
            BookingResDTO bookingResDTO = MapperUtil.mapper(booking, BookingResDTO.class);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bookingResDTO.setCreateDate(formatter.format(booking.getCreatedAt()));
            bookingResDTOS.add(bookingResDTO);
        }
        return CommonFunction.successOutput(bookingResDTOS);
    }

    @Override
    public BaseOutput getRevenueBooking(long userId) {
        List<Booking> bookingList = bookingRepository.getBookingByAgentId(userId);
        int countComplete = 0;
        int countCancel = 0;
        double totalRevenue = 0;
        for (Booking booking : bookingList) {
            if (booking.isStatus()) {
                countComplete ++;
            }
            if (booking.isCancel()) {
                countCancel ++;
            }
            totalRevenue += booking.getTotalPrice();
        }
        int countHappening = bookingList.size() - countComplete - countCancel;
        BookingAnalyzeDTO analyzeDTO = new BookingAnalyzeDTO(bookingList.size(), countCancel, countHappening,
                countComplete, totalRevenue);
        return CommonFunction.successOutput(analyzeDTO);
    }

    @Override
    public BaseOutput cancelBooking(long userId, long bookingId) {
        List<Booking> bookingList = bookingRepository.getBookingByAgentId(userId);
        for (Booking booking : bookingList) {
            if (booking.getId() == bookingId) {
                booking.setCancel(true);
                return CommonFunction.successOutput(booking);
            }
        }
        throw new ResourceNotFoundException("Booking or userId " + userId, "bookingId", bookingId);
    }

}
