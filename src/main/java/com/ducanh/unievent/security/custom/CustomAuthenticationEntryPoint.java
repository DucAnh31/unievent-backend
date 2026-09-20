package com.ducanh.unievent.security.custom;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.common.ErrorDetail;
import com.ducanh.unievent.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;


@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse apiResponse = ApiResponse.error(
                ErrorCode.UNAUTHORIZED.getDefaultMessage(),
                ErrorDetail.of(ErrorCode.UNAUTHORIZED.name()));

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
