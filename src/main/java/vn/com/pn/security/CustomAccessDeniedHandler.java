package vn.com.pn.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.LogMessageConstants;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.output.BaseOutput;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static Log logger = LogFactory.getLog(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        // loi khong co quyen
        logger.error(LogMessageConstants.INCORRECT_ROLE);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        BaseOutput roleError = CommonFunction.errorLogic(CommonConstants.HTTP_STATUS_CODE.FORBIDDEN,
                ScreenMessageConstants.INCORRECT_ROLE);
        response.getWriter().write(CommonFunction.convertToJSONString(roleError));
    }
}
