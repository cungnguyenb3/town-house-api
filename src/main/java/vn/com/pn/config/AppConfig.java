package vn.com.pn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.com.pn.domain.ForgotPasswordCode;
import vn.com.pn.domain.User;

import java.util.concurrent.ThreadLocalRandom;

//@Configuration
public class AppConfig {

//    @Autowired
//    private ForgotPasswordCode forgotPasswordCode;
//
//    @Bean("getForgotPassword")
//    public ForgotPasswordCode getForgotPasswordCode() {
//        ForgotPasswordCode forgotPasswordCode =new ForgotPasswordCode();
//        int randomNum = ThreadLocalRandom.current().nextInt(100000, 999998 + 1);
//        forgotPasswordCode.setCode(String.valueOf(randomNum));
//        return forgotPasswordCode;
//    }
}
