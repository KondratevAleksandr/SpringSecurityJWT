package com.example.SpringSecurityJWT.controller;

import com.example.SpringSecurityJWT.dto.ReqResponse;
import com.example.SpringSecurityJWT.service.AuthService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class AuthController {

    private AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signup")
    public ResponseEntity<ReqResponse> signUp(@RequestBody ReqResponse signUpRequest) {
        logger.info("User '{}' signUp in successfully", signUpRequest.getUsername());
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<ReqResponse> signIn(@RequestBody ReqResponse signInRequest) {
        logger.info("User '{}' signIn in successfully", signInRequest.getUsername());
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ReqResponse> refreshToken(@RequestBody ReqResponse refreshTokenRequest) {
        logger.info("User '{}' refresh token successfully", refreshTokenRequest.getUsername());
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
}
