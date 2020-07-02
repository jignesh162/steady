package com.steady.nifty.strategy.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steady.nifty.strategy.exception.ErrorResponse;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Constructs the REST response for authentication failures that happen in the AuthTokenFilter
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        log.debug("AuthenticationError", authException);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON.toString());
        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED);
        error.setDetail("Unauthorized");
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
