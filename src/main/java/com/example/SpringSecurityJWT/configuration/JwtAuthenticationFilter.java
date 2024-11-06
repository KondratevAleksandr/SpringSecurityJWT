package com.example.SpringSecurityJWT.configuration;

import com.example.SpringSecurityJWT.service.JWTUtils;
import com.example.SpringSecurityJWT.service.OurUserDetailedService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private OurUserDetailedService ourUserDetailedService;

    // Метод, выполняемый для каждого HTTP запроса
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // Шаг 1: Извлечение заголовка авторизации из запроса
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        // Шаг 2: Проверка наличия заголовка авторизации
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Шаг 3: Извлечение токена из заголовка
        jwtToken = authHeader.substring(7);

        // Шаг 4: Извлечение имени пользователя из JWT токена
        username = jwtUtils.extractUsername(jwtToken);

        // Шаг 5: Проверка валидности токена и аутентификации
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = ourUserDetailedService.loadUserByUsername(username);

            //проверка блокировки аккаунта
            if (userDetails != null && !userDetails.isAccountNonLocked()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Account is locked");
                return;
            }

            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                // Шаг 6: Создание нового контекста безопасности
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                SecurityContextHolder.setContext(securityContext);

                ourUserDetailedService.resetFailedLoginAttempts(userDetails.getUsername());
            } else {
                ourUserDetailedService.increaseFailedLoginAttempts(username);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
                return;
            }
        }
        // Шаг 7: Передача запроса на дальнейшую обработку в фильтрующий цепочке
        filterChain.doFilter(request, response);
    }
}