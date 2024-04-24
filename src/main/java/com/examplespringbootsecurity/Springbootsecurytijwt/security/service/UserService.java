package com.examplespringbootsecurity.Springbootsecurytijwt.security.service;

import com.examplespringbootsecurity.Springbootsecurytijwt.dto.ResponseBase;
import com.examplespringbootsecurity.Springbootsecurytijwt.dto.UserDTO;
import com.examplespringbootsecurity.Springbootsecurytijwt.models.User;

public interface UserService {


    ResponseBase getusers();

    ResponseBase getUserDetail();

    ResponseBase saveDTO(UserDTO userDTO);

    ResponseBase update(Long id, User newUser);

    ResponseBase  delete(Long id);

    ResponseBase changePw(Long id, String ps , String newPw);
}
