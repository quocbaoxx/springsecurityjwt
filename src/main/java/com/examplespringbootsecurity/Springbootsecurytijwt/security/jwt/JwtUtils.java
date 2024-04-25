package com.examplespringbootsecurity.Springbootsecurytijwt.security.jwt;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import com.examplespringbootsecurity.Springbootsecurytijwt.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
    // Khởi tạo một Logger để ghi lại thông báo lỗi hoặc thông tin
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${bezkoder.app.jwtCookieName}")
    private String jwtCookie;

    // Lấy giá trị jwtSecret từ tệp application.properties
    @Value("${quocbao.app.jwtSecret}")
    private String jwtSecret;

    // Lấy giá trị jwtSecret từ tệp application.properties
    @Value("${quocbao.app.jwtExpirationMs}")//24h
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


    public ResponseCookie getCleanJwtCookie() {
        // Tạo một ResponseCookie mà không chứa giá trị, dùng để xóa cookie JWT khỏi trình duyệt
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        // Lấy giá trị của cookie JWT từ HttpServletRequest nếu có
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();// Trả về giá trị của cookie JWT
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        // Tạo một cookie JWT mới cho người dùng được cung cấp
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api")
                                                 .maxAge(24 * 60 * 60) // Thời gian sống của cookie (24 giờ)
                                                 .httpOnly(true)  // Chỉ sử dụng trên HTTP
                                                 .build();
        return cookie;
    }

    public String generateTokenFromUsername(String username) {
        // Tạo JWT từ tên người dùng (username)
        return Jwts.builder()
                .setSubject(username)// Thiết lập chủ đề của JWT
                .setIssuedAt(new Date()) // Thiết lập thời gian phát hành
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))// Thiết lập thời gian hết hạn
                .signWith(key(), SignatureAlgorithm.HS256)// Ký JWT bằng thuật toán HS256 và key được cung cấp
                .compact();// Kết xuất JWT thành một chuỗi
    }

}