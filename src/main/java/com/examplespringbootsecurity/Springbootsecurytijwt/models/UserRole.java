package com.examplespringbootsecurity.Springbootsecurytijwt.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="tbl_user_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRole   {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;

    @Column(name ="user_id")
    private Integer user_id;

    @Column(name ="role_id")
    private Integer role_id;
}
