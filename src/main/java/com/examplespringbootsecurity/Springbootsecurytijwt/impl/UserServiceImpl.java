package com.examplespringbootsecurity.Springbootsecurytijwt.impl;

import com.examplespringbootsecurity.Springbootsecurytijwt.dto.ResponseBase;
import com.examplespringbootsecurity.Springbootsecurytijwt.dto.UserDTO;
import com.examplespringbootsecurity.Springbootsecurytijwt.models.ERole;
import com.examplespringbootsecurity.Springbootsecurytijwt.models.Role;
import com.examplespringbootsecurity.Springbootsecurytijwt.models.User;
import com.examplespringbootsecurity.Springbootsecurytijwt.repository.RoleRepository;
import com.examplespringbootsecurity.Springbootsecurytijwt.repository.UserRepository;
import com.examplespringbootsecurity.Springbootsecurytijwt.security.service.UserService;
import com.examplespringbootsecurity.Springbootsecurytijwt.validate.PhoneException;
import com.examplespringbootsecurity.Springbootsecurytijwt.validate.ValidatePhone;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserServiceImpl  implements UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    PasswordEncoder encoder;


    @Override
    public List<User> getusers() {
        List<User> user = userRepository.findAll();
        return user;
    }

    @Override
    public List<UserDTO> getUserDetail() {
        List<UserDTO> userDTOS = userRepository.findAllDTO();
        for (UserDTO userDTO : userDTOS) {
            List<Role> roles = roleRepository.findByUserId(Math.toIntExact(userDTO.getId()));
            userDTO.setRoleList(roles);
        }
        return userDTOS;
    }



    @Override
    public ResponseBase saveDTO(UserDTO userDTO, List<String> strRoles) {
        ResponseBase response = new ResponseBase();

        // Map DTO to User entity
        User user = modelMapper.map(userDTO, User.class);

        // Validate age
        if (userDTO.getAge() < 0 || userDTO.getAge() > 99) {
            response.setMessage("Số tuổi không hợp lệ");
            return response;
        }

        // Validate phone
        ValidatePhone validatePhone = new ValidatePhone();
        try {
            validatePhone.checkPhone(userDTO.getPhone());
        } catch (PhoneException ex) {
            response.setMessage(ex.getMessage());
            return response;
        }

        // Set password and encode
        user.setPassword(encoder.encode(userDTO.getPassword()));

        // Set user roles
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            user.setRoles(Collections.singleton(userRole));
        } else {
            Set<Role> roles = new HashSet<>();
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
                        Role defaultRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(defaultRole);
                }
            });
            user.setRoles(roles);
        }

        // Save user entity
        response.setData(userRepository.save(user));
        response.setMessage("Yêu cầu thành công");
        response.setCode("200");

        return response;
    }


    public List<User> update(Long id, User newUser) {
        List<User> updatedUsers = new ArrayList<>();

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return updatedUsers;
        }

        User user = optionalUser.get();

        user.setUsername(newUser.getUsername());
        user.setPhone(newUser.getPhone());

        updatedUsers.add(userRepository.save(user));

        return updatedUsers;
    }


    public Boolean delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(false);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ResponseBase changePw(Long id, String ps , String newPw) {
        ResponseBase responseBase = new ResponseBase();
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){
            responseBase.setMessage("ID không tồn tại!");
            return responseBase;
        }
        User user = optionalUser.get();

        if(!encoder.matches(ps , user.getPassword())){
            responseBase.setMessage("Mật khẩu cũ k đúng");
            return  responseBase;
        }

        user.setPassword(encoder.encode(newPw));
        userRepository.save(user);

        responseBase.setMessage("Mật khẩu đã được thay đổi thành công!");
        return responseBase;
    }


}