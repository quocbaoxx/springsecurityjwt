package com.examplespringbootsecurity.Springbootsecurytijwt.security.service;

import com.examplespringbootsecurity.Springbootsecurytijwt.dto.ResponseBase;
import com.examplespringbootsecurity.Springbootsecurytijwt.dto.UserDTO;
import com.examplespringbootsecurity.Springbootsecurytijwt.models.User;

import java.util.List;

public interface UserService {


    List<User> getusers();

    List<UserDTO> getUserDetail();

    ResponseBase saveDTO(UserDTO userDTO, List<String> strRoles);

    List<User> update(Long id, User newUser);

    Boolean  delete(Long id);

    ResponseBase changePw(Long id, String ps , String newPw);
}
