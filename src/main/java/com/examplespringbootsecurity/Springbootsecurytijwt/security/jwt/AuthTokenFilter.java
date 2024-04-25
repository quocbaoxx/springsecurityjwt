package com.examplespringbootsecurity.Springbootsecurytijwt.security.jwt;


import java.io.IOException;


import com.examplespringbootsecurity.Springbootsecurytijwt.security.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Khởi tạo một Logger để ghi lại các thông báo lỗi hoặc thông tin
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Kiểm tra xem yêu cầu có chứa token JWT không và token đó có hợp lệ không
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Lấy tên người dùng từ token JWT
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Tải thông tin người dùng từ UserDetailsServiceImpl dựa trên tên người dùng
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Tạo đối tượng Authentication bằng cách sử dụng thông tin người dùng và các quyền của họ
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                // Đặt thông tin chi tiết về xác thực cho đối tượng Authentication
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        // Kiểm tra xem tiêu đề "Authorization" có chứa token JWT không và trả về token nếu có
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
