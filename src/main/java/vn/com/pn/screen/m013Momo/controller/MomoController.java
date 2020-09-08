package vn.com.pn.screen.m013Momo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.exception.MoMoException;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m013Momo.dto.MomoIPNResponseDTO;
import vn.com.pn.screen.m013Momo.request.MomoBasicInfoRequest;
import vn.com.pn.screen.m013Momo.request.MomoIpnPaymentRequest;
import vn.com.pn.screen.m013Momo.service.MomoService;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
public class MomoController {
    private static Log logger = LogFactory.getLog(MomoController.class);

    @Autowired
    private MomoService momoService;

    @RequestMapping(value = CommonConstants.API_URL_CONST.MOMO_BASIC_INFO, method = RequestMethod.POST)
    public ResponseEntity<?> getBasicInfo(@RequestBody MomoBasicInfoRequest request) throws JsonProcessingException {
        return momoService.sendRequestPayment(request);
    }

    @RequestMapping(value = CommonConstants.API_URL_CONST.MOMO_GET_BASIC_INFO, method = RequestMethod.GET)
    public BaseOutput getAllBasicRequest() {
        return momoService.getAllMomoBasicRequest();
    }

    @RequestMapping(value = CommonConstants.API_URL_CONST.MOMO_PAYMENT_CONFIRM, method = RequestMethod.POST)
    public MomoIPNResponseDTO paymentConfirm(@RequestBody MomoIpnPaymentRequest request) throws MoMoException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        return momoService.validateIPN(request);
    }
}
