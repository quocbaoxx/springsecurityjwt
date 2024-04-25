package com.examplespringbootsecurity.Springbootsecurytijwt.security.jwt;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import com.examplespringbootsecurity.Springbootsecurytijwt.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    // Khởi tạo một Logger để ghi lại thông báo lỗi hoặc thông tin
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);


    // Lấy giá trị jwtSecret từ tệp application.properties
    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    // Lấy giá trị jwtSecret từ tệp application.properties
    @Value("${bezkoder.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Phương thức để tạo JWT token từ thông tin xác thực của người dùng
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, key())
                .compact();
    }

    // Phương thức để tạo Key từ jwtSecret
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));    }

    // Phương thức để lấy tên người dùng từ JWT token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }



    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}