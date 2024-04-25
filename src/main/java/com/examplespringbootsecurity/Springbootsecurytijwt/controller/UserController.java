package com.examplespringbootsecurity.Springbootsecurytijwt.controller;


import com.examplespringbootsecurity.Springbootsecurytijwt.dto.ResponseBase;
import com.examplespringbootsecurity.Springbootsecurytijwt.dto.UserDTO;
import com.examplespringbootsecurity.Springbootsecurytijwt.models.ERole;
import com.examplespringbootsecurity.Springbootsecurytijwt.models.Role;
import com.examplespringbootsecurity.Springbootsecurytijwt.models.User;
import com.examplespringbootsecurity.Springbootsecurytijwt.repository.RoleRepository;
import com.examplespringbootsecurity.Springbootsecurytijwt.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Past;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService ;

    @Autowired
    private RoleRepository roleRepository;




    @GetMapping("/get-users")
    public ResponseBase getUser(){
        return new ResponseBase(userService.getusers());
    }



    @GetMapping("/get-user-detail")
    public  ResponseBase getUserDetail(){
        return new ResponseBase(userService.getUserDetail()) ;
    }

    @PutMapping("/update-user/{id}")
    public ResponseBase updateUser(@PathVariable Long id,
                                                   @RequestBody User newUser) {
        List<User> updatedUsers = userService.update(id, newUser);
        ResponseBase response = new ResponseBase();

        if (updatedUsers.isEmpty()) {
            response.setMessage("Không tìm thấy người dùng để cập nhật");
            response.setCode("404");
            return response;
        } else {
            response.setData(updatedUsers);
            response.setMessage("Cập nhật người dùng thành công");
            response.setCode("200");
            return response;
        }
    }

    @PostMapping("/add-user")
    public ResponseBase saveUser(@RequestBody UserDTO userDTO,
                                                 @RequestParam(required = false) List<String> strRoles) {
        ResponseBase response = userService.saveDTO(userDTO, strRoles);
        return response;
    }




    @PostMapping("/change-password/{id}")
    public ResponseBase changePassword(@PathVariable Long id,
                                                       @RequestParam("currentPassword") String currentPassword,
                                                       @RequestParam("newPassword") String newPassword) {
        ResponseBase responseBase = userService.changePw(id, currentPassword, newPassword);
            return responseBase;
    }



    @DeleteMapping("/{id}")
    public ResponseBase deleteUser(@PathVariable Long id) {
        ResponseBase response = new ResponseBase();
        Boolean isDeleted = userService.delete(id);

        if (isDeleted) {
            response.setMessage("Xóa người dùng thành công");
            response.setCode("200");
            return response;
        } else {
            response.setMessage("Không tìm thấy người dùng để xóa");
            response.setCode("404");
            return response;
        }
    }





}
