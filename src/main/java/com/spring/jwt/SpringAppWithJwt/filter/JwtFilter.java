package com.spring.jwt.SpringAppWithJwt.filter;

import com.spring.jwt.SpringAppWithJwt.models.Employee;
import com.spring.jwt.SpringAppWithJwt.repository.EmployeeRepository;
import com.spring.jwt.SpringAppWithJwt.services.CustomUserDetailsService;
import com.spring.jwt.SpringAppWithJwt.utils.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";
    private final JwtTokenService jwtTokenService;
    private final EmployeeRepository employeeRepository;

    private final CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(AUTHORIZATION);
        String jwtToken;
        String username;
        if (token == null || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = token.substring(7);
        username = jwtTokenService.getAccessClaims(jwtToken).getSubject();


        if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
            Employee employee = (Employee) customUserDetailsService.loadUserByUsername(username);

            if (jwtTokenService.validateAccessToken(jwtToken) && employee.getJwtRefreshToken() == jwtToken) {
                // Проверка и установка пользователя в сессию через Secutiry contex
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                employee.getUsername(),employee.getPassword(),employee.getRoles()
                        );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }


        }
        filterChain.doFilter(request, response);
    }
}
