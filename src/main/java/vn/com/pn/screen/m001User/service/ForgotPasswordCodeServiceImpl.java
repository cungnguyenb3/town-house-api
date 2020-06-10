package vn.com.pn.screen.m001User.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.screen.m001User.controller.UserController;
import vn.com.pn.screen.m001User.entity.ForgotPasswordCode;
import vn.com.pn.screen.m001User.repository.ForgotPasswordCodeRepository;

import java.util.List;

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
