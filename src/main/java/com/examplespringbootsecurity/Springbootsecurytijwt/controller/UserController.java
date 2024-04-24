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
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService ;

    @Autowired
    private RoleRepository roleRepository;




    @GetMapping("/get-users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseBase getUser(){
        return userService.getusers();
    }



    @GetMapping("/get-user-detail")
    public  ResponseBase getUserDetail(){
        return  userService.getUserDetail();
    }

    @PostMapping ("/add-user")
    public  ResponseBase saveDTO(@RequestBody UserDTO userDTO){
        return  userService.saveDTO(userDTO);
    }
    @PutMapping("/update-user/{id}")
    public  ResponseBase update(@PathVariable("id")Long id,
                                @RequestBody User user){
        return  userService.update(id,user);
    }

    @DeleteMapping("/{id}")
    public  ResponseBase  delete(@PathVariable("id") Long id){
         return userService.delete(id);
    }



    @PostMapping("/change-password/{id}")
    public ResponseEntity<ResponseBase> changePassword(@PathVariable Long id,
                                                       @RequestParam("currentPassword") String currentPassword,
                                                       @RequestParam("newPassword") String newPassword) {
        ResponseBase responseBase = userService.changePw(id, currentPassword, newPassword);
            return ResponseEntity.ok(responseBase);
    }







}
