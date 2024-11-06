package com.example.SpringSecurityJWT.service;

import com.example.SpringSecurityJWT.entity.OurUser;
import com.example.SpringSecurityJWT.repository.OurUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OurUserDetailedService implements UserDetailsService {

    private OurUserRepository ourUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ourUserRepository.findByUsername(username).orElseThrow();
    }

    public void increaseFailedLoginAttempts(String username) {
        OurUser ourUser = ourUserRepository.findByUsername(username).orElseThrow();
        if (ourUser != null) {
            ourUser.setFailedLoginAttempts(ourUser.getFailedLoginAttempts() + 1);
            if (ourUser.getFailedLoginAttempts() >= 5) {
                ourUser.setAccountNonLocked(false);
            }
            ourUserRepository.save(ourUser);
        }
    }

    public void resetFailedLoginAttempts(String username) {
        OurUser ourUser = ourUserRepository.findByUsername(username).orElseThrow();
        if (ourUser != null) {
            ourUser.setFailedLoginAttempts(0);
            ourUser.setAccountNonLocked(true);
            ourUserRepository.save(ourUser);
        }
    }
}
