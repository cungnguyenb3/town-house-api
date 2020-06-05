package vn.com.pn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.com.pn.api.request.BookingCancelRequest;
import vn.com.pn.api.request.BookingInsertRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.BookingCalculatePriceDTO;
import vn.com.pn.common.dto.BookingCancelDTO;
import vn.com.pn.common.dto.BookingDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.User;
import vn.com.pn.security.AuthService;
import vn.com.pn.service.booking.BookingService;
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

    @ApiOperation(value = "View a list bookings", response = BaseOutput.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
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
                    required = true, dataType = "string", paramType = "header") })
    @ApiOperation(value = "API request booking", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_ROOT, method = RequestMethod.POST)
    public BaseOutput insert(@Valid @RequestBody BookingInsertRequest request){
        logger.info("========== BookingController.insert START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            User userLogin = authService.getLoggedUser();
            BookingDTO bookingDTO = MapperUtil.mapper(request, BookingDTO.class);
            BaseOutput response = bookingService.insert(bookingDTO, userLogin);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== BookingController.insert END ==========");
            return response;
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    @ApiOperation(value = "Confirm  booking request", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_CONFIRM_REQUEST, method = RequestMethod.PUT)
    public ResponseEntity<?> confirmBookingRequest(@PathVariable String bookingId) {
        logger.info("========== BookingController.confirmBookingRequest START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(bookingId));
        User userLogin = authService.getLoggedUser();
        BaseOutput response = bookingService.confirmBookingRequest(bookingId, userLogin.getId());
        logger.info("======= BookingController.confirmBookingRequest END========");
        return ResponseEntity.ok(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Booking request successful", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_REQUEST_SUCCESS, method = RequestMethod.PUT)
    public ResponseEntity<?> bookingRequestSuccess(@PathVariable String bookingId) {
        logger.info("========== BookingController.bookingRequestSuccess START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(bookingId));
        BaseOutput response = bookingService.confirmBookingPaid(bookingId);
        logger.info("======= BookingController.bookingRequestSuccess END========");
        return ResponseEntity.ok(response);
    }

//    @ApiOperation(value = "Update Booking status for authentication user", response = BaseOutput.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
//                    required = true, dataType = "string", paramType = "header") })
//    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_CANCEL, method = RequestMethod.PUT)
//    public BaseOutput bookingCancel(@Valid @PathVariable String id, @RequestBody BookingCancelRequest request){
//        logger.info("========== BookingController.bookingCancel START ==========");
//        logger.info("request: " + CommonFunction.convertToJSONString(request));
//        try {
//            BookingCancelDTO bookingCancelDTO = new BookingCancelDTO();
//            User userLogin = authService.getLoggedUser();
//            bookingCancelDTO.setId(id);
//            bookingCancelDTO.setStatus(request.getStatus());
//            BaseOutput response = bookingService.bookingCancel(bookingCancelDTO, userLogin);
//            logger.info(CommonFunction.convertToJSONStringResponse(response));
//            logger.info("========== BookingController.bookingCancel END ==========");
//            return response;
//        } catch (Exception e) {
//            logger.error(ScreenMessageConstants.FAILURE, e);
//            return CommonFunction.failureOutput();
//        }
//    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header") })
    @ApiOperation(value = "Api add new booking", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.BOOKING_CALCULATE_PRICE, method = RequestMethod.POST)
    public BaseOutput calculatePrice(@Valid @RequestBody BookingCalculatePriceDTO request){
        logger.info("========== BookingController.calculatePrice START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            BookingCalculatePriceDTO bookingCalculatePriceDTO = MapperUtil.mapper(request, BookingCalculatePriceDTO.class);
            BaseOutput response = bookingService.calculatePrice(bookingCalculatePriceDTO);
            logger.info(CommonFunction.convertToJSONStringResponse(response));
            logger.info("========== BookingController.calculatePrice END ==========");
            return response;
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
