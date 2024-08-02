package com.management_system.utilities.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.utilities.constant.ConstantValue;
import com.management_system.utilities.constant.enumuration.CredentialEnum;
import com.management_system.utilities.constant.enumuration.TokenType;
import com.management_system.utilities.entities.TokenInfo;
import com.management_system.utilities.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.*;
import java.util.function.Function;


@RequiredArgsConstructor
@Service
public class JwtUtils {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_PREFIX = "Bearer ";
    final RefreshTokenRepository refreshTokenRepo;
    final CredentialsUtils credentialsUtils;
    final DbUtils dbUtils;

    public TokenInfo getTokenInfoFromHttpRequest(HttpServletRequest request) {
        return getRefreshTokenInfoFromJwt(getJwtFromRequest(request));
    }


    public boolean isRefreshTokenValid(String token) {
        final TokenInfo tokenInfo = refreshTokenRepo.getRefreshTokenInfoByToken(token);

        return tokenInfo != null &&
                tokenInfo.getToken() != null &&
                !tokenInfo.getToken().isBlank();
    }


    public void createRefreshTokenForAccount(String userName, String role) {
        TokenInfo tokenInfo = refreshTokenRepo.getRefreshTokenInfoByUserName(userName);

        if (tokenInfo != null) {
            String newRefreshToken = generateJwt(tokenInfo, TokenType.REFRESH_TOKEN);
            Map<String, Object> map = new HashMap<>();
            map.put("token", newRefreshToken);

            dbUtils.updateSpecificFields("id", tokenInfo.getId(), map, TokenInfo.class);

        } else {
            tokenInfo = TokenInfo.builder()
                    .id(UUID.randomUUID().toString())
                    .userName(userName)
                    .roles(Collections.singletonList(role))
                    .build();
            String newRefreshToken = generateJwt(tokenInfo, TokenType.REFRESH_TOKEN);
            tokenInfo.setToken(newRefreshToken);

            refreshTokenRepo.save(tokenInfo);
        }
    }


    public String generateJwt(TokenInfo refreshToken, TokenType type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> claims = mapper.convertValue(refreshToken, Map.class);

            return Jwts
                    .builder()
                    .setClaims(claims)
                    .setSubject((String) claims.get("userName"))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(
                            type == TokenType.REFRESH_TOKEN
                                    ? new Date(System.currentTimeMillis() + ConstantValue.ONE_WEEK_MILLISECOND)
                                    : type == TokenType.JWT
                                    ? new Date(System.currentTimeMillis() + ConstantValue.SIX_HOURS_MILLISECOND)
                                    : new Date()
                    )
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean isTokenExpired(String token) {
        return getJwtExpiration(token).before(new Date());
    }


    public TokenInfo getRefreshTokenInfoFromJwt(String jwt) {
        Claims claims = getAllClaimsFromJwt(jwt);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> claimMap = mapper.convertValue(claims, Map.class);
        claimMap.remove("iat");
        claimMap.remove("exp");

        return mapper.convertValue(claimMap, TokenInfo.class);
    }


    public Date getJwtExpiration(String jwt) {
        return getSingleClaimFromJwt(jwt, Claims::getExpiration);
    }


    public String getJwtFromRequest(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");

        if (jwtToken == null) {
            return null;
        } else {
            return request.getHeader("Authorization").substring(7);
        }
    }


    public void setJwtToClientCookie(String jwt) {
        var cookie = new Cookie(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + jwt);
        cookie.setMaxAge(ConstantValue.SIX_HOURS_SECOND);
        cookie.setPath("/");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        assert attributes != null;
        assert attributes.getResponse() != null;

        attributes.getResponse().addCookie(cookie);
    }


    public <T> T getSingleClaimFromJwt(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromJwt(jwt);
        return claimsResolver.apply(claims);
    }


    public Claims getAllClaimsFromJwt(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }


    public Key getSigningKey() {
        String secretSigningKey = credentialsUtils.getCredentials(CredentialEnum.SECRET_SIGNING_KEY.name());
        byte[] keyBytes = Decoders.BASE64.decode(secretSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
