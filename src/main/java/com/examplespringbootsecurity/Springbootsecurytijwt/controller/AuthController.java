package com.examplespringbootsecurity.Springbootsecurytijwt.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.examplespringbootsecurity.Springbootsecurytijwt.models.ERole;
import com.examplespringbootsecurity.Springbootsecurytijwt.models.Role;

import com.examplespringbootsecurity.Springbootsecurytijwt.models.User;
import com.examplespringbootsecurity.Springbootsecurytijwt.payload.request.LoginRequest;
import com.examplespringbootsecurity.Springbootsecurytijwt.payload.request.SignupRequest;
import com.examplespringbootsecurity.Springbootsecurytijwt.payload.response.JwtResponse;
import com.examplespringbootsecurity.Springbootsecurytijwt.payload.response.MessageResponse;
import com.examplespringbootsecurity.Springbootsecurytijwt.payload.response.UserInfoResponse;
import com.examplespringbootsecurity.Springbootsecurytijwt.repository.RoleRepository;
import com.examplespringbootsecurity.Springbootsecurytijwt.repository.UserRepository;
import com.examplespringbootsecurity.Springbootsecurytijwt.security.jwt.JwtUtils;
import com.examplespringbootsecurity.Springbootsecurytijwt.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Xác thực người dùng bằng tên người dùng và mật khẩu từ yêu cầu đăng nhập
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Thiết lập thông tin xác thực vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Tạo JWT token từ thông tin xác thực
        String jwt = jwtUtils.generateJwtToken(authentication);
        // Lấy chi tiết người dùng sau khi xác thực
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Tạo ResponseCookie chứa JWT
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        // Lấy danh sách vai trò của người dùng
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Trả về phản hồi thành công với thông tin người dùng và JWT token
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())// Thiết lập cookie chứa JWT trong header
                .body(new UserInfoResponse(jwt,userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));// Trả về thông tin người dùng và JWT trong phản hồi


//        return ResponseEntity.ok(new JwtResponse(jwt,
//                userDetails.getId(),
//                userDetails.getUsername(),
//                userDetails.getEmail(),
//                roles));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }



}
