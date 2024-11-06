package com.example.SpringSecurityJWT.repository;


import com.example.SpringSecurityJWT.entity.OurUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OurUserRepository extends JpaRepository<OurUser, Integer> {
    Optional<OurUser> findByUsername(String username);
}
