package vn.com.pn.screen.f002Booking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.screen.f002Booking.request.BookingRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.f002Booking.dto.BookingCalculatePriceDTO;
import vn.com.pn.screen.f002Booking.dto.BookingDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m001User.repository.UserRepository;
import vn.com.pn.security.AuthService;
import vn.com.pn.screen.f002Booking.service.BookingService;
import vn.com.pn.utils.MapperUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
@Api(value = "bookings", description = "Manage Booking")
public class BookingController {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "View a list bookings", response = BaseOutput.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_ROOT, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== BookingController.getAll START ==========");
        BaseOutput response = bookingService.getAll();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== BookingController.getAll END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "API request booking", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody BookingRequest request) {
        logger.info("========== BookingController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        User userLogin = userRepository.findById(authService.getLoggedUser().getId()).orElse(null);
        BookingDTO bookingDTO = MapperUtil.mapper(request, BookingDTO.class);
        BaseOutput response = bookingService.insert(bookingDTO, userLogin);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== BookingController.insert END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Accept booking for host agent", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_CONFIRM_REQUEST, method = RequestMethod.PUT)
    public ResponseEntity<?> confirmBookingRequest(@Valid @PathVariable String bookingId) throws JsonProcessingException {
        logger.info("========== BookingController.confirmBookingRequest START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(bookingId));
        User userLogin = authService.getLoggedUser();
        BaseOutput response = bookingService.confirmBookingRequest(bookingId, userLogin.getId());
        logger.info("======= BookingController.confirmBookingRequest END========");
        return ResponseEntity.ok(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Set booking is paid or not", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.ADMIN_BOOKING_REQUEST_SUCCESS, method = RequestMethod.PUT)
    public ResponseEntity<?> bookingRequestSuccess(@Valid @PathVariable String bookingId) throws JsonProcessingException {
        logger.info("========== BookingController.bookingRequestSuccess START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(bookingId));
        BaseOutput response = bookingService.confirmBookingPaid(bookingId);
        logger.info("======= BookingController.bookingRequestSuccess END========");
        return ResponseEntity.ok(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Api add new booking", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_CALCULATE_PRICE, method = RequestMethod.POST)
    public BaseOutput calculatePrice(@Valid @RequestBody BookingCalculatePriceDTO request) {
        logger.info("========== BookingController.calculatePrice START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        BookingCalculatePriceDTO bookingCalculatePriceDTO = MapperUtil.mapper(request, BookingCalculatePriceDTO.class);
        BaseOutput response = bookingService.calculatePrice(bookingCalculatePriceDTO);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== BookingController.calculatePrice END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Api get booking by id", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_ID, method = RequestMethod.GET)
    public BaseOutput getBookingById(@Valid @PathVariable long id) {
        logger.info("========== BookingController.getBookingById START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(id));
        BaseOutput response = bookingService.getBookingById(id);
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== BookingController.getBookingById END ==========");
        return response;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Api get booking by user for calendar", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_USER_CALENDER, method = RequestMethod.GET)
    public ResponseEntity<?> getBookingCalendarByUser() {
        User userLogin = authService.getLoggedUser();
        return bookingService.getBookingByCurrentDateAndUser(userLogin.getId());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Api get all booking by user user agent", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_USER, method = RequestMethod.GET)
    public BaseOutput getAllBookingByHostAgent() {
        User userLogin = authService.getLoggedUser();
        return bookingService.getAllBookingFromAgent(userLogin.getId());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Analytics booking", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_ANALYSING, method = RequestMethod.GET)
    public BaseOutput analystBooking() {
        User userLogin = authService.getLoggedUser();
        return bookingService.getRevenueBooking(userLogin.getId());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "Cancel booking", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_CANCEL, method = RequestMethod.PUT)
    public BaseOutput cancelBooking(@PathVariable long id) {
        User userLogin = authService.getLoggedUser();
        return bookingService.cancelBooking(userLogin.getId(), id);
    }
}