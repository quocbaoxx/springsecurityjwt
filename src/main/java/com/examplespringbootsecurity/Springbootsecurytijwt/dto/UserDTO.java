package com.examplespringbootsecurity.Springbootsecurytijwt.dto;


import com.examplespringbootsecurity.Springbootsecurytijwt.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String password;
    private Integer age;
    private String phone;
    private Boolean status ;
    private List<Role> roleList = new ArrayList<>();




    public UserDTO(Long id, String username, String email, String password ,Integer age, String phone, Boolean status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.status = status;
    }


    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;

    }


}
