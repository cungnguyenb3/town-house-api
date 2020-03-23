package vn.com.pn.common.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindingResult;
import vn.com.pn.common.output.BaseOutput;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CommonFunction {
    private static Log logger = LogFactory.getLog(CommonFunction.class);

    public static BaseOutput successOutput(List<Object> list){
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

    public static BaseOutput failureOutput() {
        logger.info("CommonFunction.failureOutput");
        BaseOutput output = new BaseOutput();
        output.setStatus(CommonConstants.STATUS.STATUS_FAILURE);
        output.setMessage(ScreenMessageConstants.FAILURE);
        return output;
    }
    public static BaseOutput errorLogic(int status, Object message) {
        logger.info("CommonFunction.failureOutput");
        BaseOutput output = new BaseOutput();
        output.setStatus(CommonConstants.STATUS.STATUS_FAILURE);
        output.setMessage(message);
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

}
