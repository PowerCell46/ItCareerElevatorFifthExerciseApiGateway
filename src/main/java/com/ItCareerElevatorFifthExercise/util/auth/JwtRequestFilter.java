package com.ItCareerElevatorFifthExercise.util.auth;

import com.ItCareerElevatorFifthExercise.DTOs.common.ErrorResponseDTO;
import com.ItCareerElevatorFifthExercise.services.interfaces.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fChain) throws ServletException, IOException {
        boolean isEndpointPublic = isPathPublic(req.getRequestURI());

        final String header = req.getHeader("Authorization");
        String username = null;
        String token = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);

        } else {
            token = req.getParameter("token");
        }

        if (token != null) {
            try {
                username = jwtUtils.extractUsername(token);

            } catch (SignatureException ex) {
                writeJsonErrorResponse(res, "Invalid token signature.");
                return;

            } catch (ExpiredJwtException ex) {
                writeJsonErrorResponse(res, "Token has expired.");
                return;

            } catch (JwtException ex) {
                writeJsonErrorResponse(res, "Invalid token.");
                return;
            }

        } else if (!isEndpointPublic) {
            writeJsonErrorResponse(res, "Missing or malformed Authorization header.");
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtUtils.validateToken(token, userDetails)) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } else if (!isEndpointPublic) {
                writeJsonErrorResponse(res, "Invalid token.");
                return;
            }
        }

        fChain.doFilter(req, res);
    }

    private boolean isPathPublic(String path) {
        Set<String> PUBLIC_ENDPOINTS = Set.of("/api/auth/register", "/api/auth/login", "/internal/deliverMessageToReceiver");

        return PUBLIC_ENDPOINTS.contains(path);
    }

    private void writeJsonErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpServletResponse.SC_UNAUTHORIZED,
                message,
                System.currentTimeMillis()
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
