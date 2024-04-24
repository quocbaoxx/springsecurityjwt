package com.examplespringbootsecurity.Springbootsecurytijwt.impl;

import com.examplespringbootsecurity.Springbootsecurytijwt.dto.ResponseBase;
import com.examplespringbootsecurity.Springbootsecurytijwt.dto.UserDTO;
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

import java.util.List;
import java.util.Optional;


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
    public ResponseBase getusers() {
        List<User> user = userRepository.findAll();
        return new ResponseBase(user);
    }

    @Override
    public ResponseBase getUserDetail() {
        List<UserDTO> userDTOS = userRepository.findAllDTO();
        for (UserDTO userDTO : userDTOS) {
            List<Role> roles = roleRepository.findByUserId(Math.toIntExact(userDTO.getId()));
            userDTO.setRoleList(roles);
        }
        return new ResponseBase(userDTOS);
    }

    @Override
    public ResponseBase saveDTO(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        ResponseBase response = new ResponseBase();
        if (userDTO.getAge()< 0 && userDTO.getAge()>99){
            response.setMessage("Số tuổi không hợp lệ");
            return  response;
        }
        //validate SDT
        ValidatePhone validatePhone = new ValidatePhone();
        try {
            validatePhone.checkPhone(userDTO.getPhone());
        } catch (PhoneException ex) {
//            System.out.println(ex.getMessage());
            response.setMessage(ex.getMessage());
            return response;
        }

        user.setPassword(encoder.encode(userDTO.getPassword()));
        response.setData(userRepository.save(user));
        response.setMessage("Yêu cầu thành công");
        response.setCode("200");

        return response;

    }

    @Override
    public ResponseBase update( Long id, User newUser) {
        ResponseBase responseBase = new ResponseBase();

        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            responseBase.setMessage("ID không tồn tại");
            return responseBase;
        }
        User user = optionalUser.get();

        user.setPhone(newUser.getPhone());
        user.setUsername(newUser.getUsername());
        user.setPhone(newUser.getPhone());
        return new ResponseBase(userRepository.save(user));
    }

    @Override
    public  ResponseBase  delete(Long id){

        ResponseBase responseBase = new ResponseBase();

        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()){
            responseBase.setMessage("Id không hợp lệ");
            return responseBase;
        }
        User user1 = user.get();
        user1.setStatus(false);
        userRepository.save(user1);


        responseBase.setMessage("Xoá thành công");
        return responseBase;
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