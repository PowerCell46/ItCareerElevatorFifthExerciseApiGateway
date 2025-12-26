package com.ItCareerElevatorFifthExercise.util.auth;

import com.ItCareerElevatorFifthExercise.DTOs.common.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ex) throws IOException {
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpServletResponse.SC_FORBIDDEN,
                "You don't have permissions to access this resource.",
                System.currentTimeMillis()
        );

        objectMapper.writeValue(res.getWriter(), errorResponse);
    }
}
