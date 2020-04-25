package vn.com.pn.service.forgotpasswordcode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.dto.ForgotPasswordInputDTO;
import vn.com.pn.controller.UserController;
import vn.com.pn.domain.ForgotPasswordCode;
import vn.com.pn.repository.forgotpasswordcode.ForgotPasswordCodeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ForgotPasswordCodeServiceImpl implements ForgotPasswordCodeService {
    private static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private ForgotPasswordCodeRepository forgotPasswordCodeRepository;

    @Override
    public List<ForgotPasswordCode> getAll() {
        logger.info("ForgotPasswordCodeServiceImpl.getAll");
        return forgotPasswordCodeRepository.findAll();
    }
}
