package vn.com.pn.common.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.validation.BindingResult;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.config.ScheduledConfig;

import java.text.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonFunction {
    private static Log logger = LogFactory.getLog(CommonFunction.class);

    public static BaseOutput successOutput(List<?> list){
        logger.info("CommonFunction.successOutput");
        BaseOutput output = new BaseOutput();
        if(list != null && list.size() > 0){
            output.setTotalRecord(list.size());
            output.setData(list);
        } else {
            output.setTotalRecord(0);
            output.setData(null);
        }
        output.setStatus(CommonConstants.STATUS.STATUS_SUCCESS);
        output.setMessage(ScreenMessageConstants.SUCCESS);
        return output;
    }

    public static BaseOutput successOutput(Object data){
        logger.info("CommonFunction.successOutput");
            BaseOutput output = new BaseOutput();
        output.setTotalRecord(1);
        output.setData(data);
        output.setStatus(CommonConstants.STATUS.STATUS_SUCCESS);
        output.setMessage(ScreenMessageConstants.SUCCESS);
        return output;
    }

    public static BaseOutput successOutput(Object data, int total){
        logger.info("CommonFunction.successOutput");
        BaseOutput output = new BaseOutput();
        output.setTotalRecord(total);
        output.setData(data);
        output.setStatus(CommonConstants.STATUS.STATUS_SUCCESS);
        output.setMessage(ScreenMessageConstants.SUCCESS);
        return output;
    }

    public static BaseOutput failureOutput() {
        logger.info("CommonFunction.failureOutput");
        BaseOutput output = new BaseOutput();
        output.setStatus(CommonConstants.STATUS.STATUS_FAILURE);
        output.setMessage(ScreenMessageConstants.FAILURE);
        return output;
    }
    public static BaseOutput errorLogic(int status, Object message) {
        logger.info("CommonFunction.errorLogic");
        BaseOutput output = new BaseOutput();
        output.setStatus(status);
        output.setMessage(message);
        output.setTotalRecord(0);
        output.setData(null);
        return output;
    }


    public static String convertToJSONString(Object object) {
        logger.info("CommonFunction.convertToJSONString");
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertToJSONStringResponse(BaseOutput object){
        logger.info("CommonFunction.convertToJSONString");
        try{
            ObjectMapper mapper = new ObjectMapper();
            return "response status: " + mapper.writeValueAsString(object.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date convertStringToDateObject(String inputDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(inputDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BaseOutput errorValidateItem(BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.getFieldErrors() != null && bindingResult.getFieldErrors().size() > 0) {
            bindingResult.getFieldErrors().stream().forEach(f -> {
                errors.add(MessageFormat.format(f.getDefaultMessage(), f.getField()));
            });
        } else if (bindingResult.getErrorCount() > 0) {
            errors.add(bindingResult.getGlobalError().getDefaultMessage());
        }
        return CommonFunction.errorLogic(CommonConstants.STATUS.STATUS_PARAM_ERROR, errors);
    }

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

    public static ScheduledTaskRegistrar setUpCronTask(LocalDateTime timestamp, Runnable runnable) {
        StringBuffer expression = new StringBuffer();
        expression.append(timestamp.getSecond()).append(" ").append(timestamp.getMinute()).append(" ")
                .append(timestamp.getHour()).append(" ").append(timestamp.getDayOfMonth()).append(" ")
                .append(timestamp.getMonth().getValue()).append(" ").append(timestamp.getDayOfWeek().getValue());

        ScheduledTaskRegistrar scheduledTaskRegistrar = new ScheduledTaskRegistrar();
        CronTask task = new CronTask(runnable, new CronTrigger(expression.toString()));
        scheduledTaskRegistrar.addCronTask(task);
        return scheduledTaskRegistrar;
    }
}
