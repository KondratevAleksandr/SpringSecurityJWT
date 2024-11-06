package com.example.SpringSecurityJWT.service;

import com.example.SpringSecurityJWT.dto.ReqResponse;
import com.example.SpringSecurityJWT.entity.OurUser;
import com.example.SpringSecurityJWT.repository.OurUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class AuthService {

    private OurUserRepository ourUserRepository;
    private JWTUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public ReqResponse signUp(ReqResponse registrationRequest) {
        ReqResponse response = new ReqResponse();
        try {
            OurUser ourUser = new OurUser();
            ourUser.setUsername(registrationRequest.getUsername());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUser.setRole(registrationRequest.getRole());
            OurUser ourUserResult = ourUserRepository.save(ourUser);
            if (ourUserResult != null && ourUserResult.getId() > 0) {
                response.setOurUser(ourUserResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public ReqResponse signIn(ReqResponse signInRequest) {
        ReqResponse response = new ReqResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signInRequest.getUsername(), signInRequest.getPassword()));
            var user = ourUserRepository.findByUsername(signInRequest.getUsername()).orElseThrow();
            System.out.println("USER is: " + user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24 hours");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public ReqResponse refreshToken(ReqResponse refreshTokenRequest) {
        ReqResponse response = new ReqResponse();
        String ourUsername = jwtUtils.extractUsername(refreshTokenRequest.getUsername());
        OurUser user = ourUserRepository.findByUsername(ourUsername).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtUtils.generateToken(user);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpirationTime("24 hours");
        }
        return response;
    }
}
