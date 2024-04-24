package com.examplespringbootsecurity.Springbootsecurytijwt.dto;


import com.examplespringbootsecurity.Springbootsecurytijwt.models.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {


    private Integer id;;
    private ERole name;


}
