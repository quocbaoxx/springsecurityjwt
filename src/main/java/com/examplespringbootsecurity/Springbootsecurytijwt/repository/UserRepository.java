package com.examplespringbootsecurity.Springbootsecurytijwt.repository;


import com.examplespringbootsecurity.Springbootsecurytijwt.dto.UserDTO;
import com.examplespringbootsecurity.Springbootsecurytijwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);


    @Query("select  new com.examplespringbootsecurity.Springbootsecurytijwt.dto.UserDTO(u.id,u.username, u.email, u.password, u.age, u.phone,u.status)" +
            "from User u")
    List<UserDTO> findAllDTO();


    @Query("select  new com.examplespringbootsecurity.Springbootsecurytijwt.dto.UserDTO(u.id,u.username, u.email)" +
            "from User u")
    List<UserDTO> getUsser();


}