package com.example.SpringSecurityJWT.dto;

import com.example.SpringSecurityJWT.entity.OurUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqResponse {

    private String token;
    private String refreshToken;
    private String expirationTime;
    private String username;
    private String password;
    private String role;
    private OurUser ourUser;
}
