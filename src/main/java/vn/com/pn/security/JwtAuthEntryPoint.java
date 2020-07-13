package vn.com.pn.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        // This is invoked when user tries to access a secured REST resource without
        // supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login
        // page' to redirect to
        // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        // NO LOGIN
        logger.error("Unauthorized error. Message - {}", e.getMessage());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(401);
        ResponseEntity<?> loginError = ResponseEntity.status(401).body("Token invalid or expired!");
        response.getWriter().write(CommonFunction.convertToJSONString(loginError));
    }
}