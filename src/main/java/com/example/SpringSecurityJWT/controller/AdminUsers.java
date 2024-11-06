package com.example.SpringSecurityJWT.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AdminUsers {


    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> userProfile() {
        return ResponseEntity.ok("Only USERS can see this");
    }

    @GetMapping("/moderator")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Object> moderatorProfile() {
        return ResponseEntity.ok("Only MODERATORS can see this");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Object> adminProfile() {
        return ResponseEntity.ok("Only ADMINS can see this");
    }
}
