package com.examplespringbootsecurity.Springbootsecurytijwt.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank//trường đó không được rỗng và phải chứa ít nhất một ký tự không phải khoảng trắng
    @Size(max = 120)
    private String password;


    @Column(name = "code", length = 60, nullable = true)
    private String code;


    @Transient
    private Set<Role> roles = new HashSet<>();
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    @Column(name ="age")
    private Integer age;


    @Column(name = "phone", length = 15, nullable = true)
    private String phone;

    @Column(name = "status", nullable = true)
    private Boolean status = Boolean.TRUE;



    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
