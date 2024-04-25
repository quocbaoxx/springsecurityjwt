package com.examplespringbootsecurity.Springbootsecurytijwt.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {


    // Khởi tạo một Logger để ghi lại thông báo lỗi
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // Ghi lại thông báo lỗi vào hệ thống ghi log
        logger.error("Unauthorized error: {}", authException.getMessage());
        // Gửi một phản hồi lỗi với mã trạng thái "401 Unauthorized" và một thông báo lỗi đơn giản
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}