package com.management_system.utilities.web.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.utilities.constant.enumuration.TokenType;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.TokenInfo;
import com.management_system.utilities.repository.RefreshTokenRepository;
import com.management_system.utilities.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenRepository refreshTokenRepo;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final TokenInfo tokenInfo;
        final SecurityContext securityContext = SecurityContextHolder.getContext();

        try {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, HEAD, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

            // if header is not null and starts with word 'Bearer' -> proceed filter
            if (authHeader != null && authHeader.startsWith("Bearer")) {
                // get jwt from header ('Bearer' length is 7 => get jwt after index 7 of header)
                jwt = jwtUtils.getJwtFromRequest(request);
                logger.info("Client jwt: " + jwt);
                String refreshToken = request.getHeader("refresh_token");

                // if jwt is expired, or user has not been authorized
                if ((jwtUtils.isTokenExpired(jwt) ||
                        securityContext == null ||
                        securityContext instanceof AnonymousAuthenticationToken) &&
                        refreshToken != null) {
                    logger.error("Client JWT expired");
                    // check refresh token from database
                    tokenInfo = refreshTokenRepo.getRefreshTokenInfoByToken(refreshToken);
                    logger.info("Got token info from db");

                    if (tokenInfo != null) {
                        String newJwtToken = jwtUtils.generateJwt(tokenInfo, TokenType.JWT);
                        logger.info("New Client JWT: " + newJwtToken);
                        jwtUtils.setJwtToClientCookie(newJwtToken);
                    }
                    else {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType("application/json");
                        response.getWriter().write(convertObjectToJson(
                                new ApiResponse("failed", "Invalid refresh token")
                        ));
                    }
                }
                else {
                    // get token info from jwt
                    tokenInfo = jwtUtils.getRefreshTokenInfoFromJwt(jwt);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            tokenInfo.getUserName(),
                            null,
                            tokenInfo.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.getWriter().write(convertObjectToJson(new ApiResponse("failed", e.getMessage())));
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().contains("/authen");
    }


    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        }
    }
}
