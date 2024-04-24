package com.examplespringbootsecurity.Springbootsecurytijwt.repository;

import com.examplespringbootsecurity.Springbootsecurytijwt.models.ERole;
import com.examplespringbootsecurity.Springbootsecurytijwt.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    @Query("select r from UserRole  ur join Role r  on ur.role_id = r.id where ur.user_id = :userId ")
    List<Role> findByUserId(@Param("userId") Integer userId);

}