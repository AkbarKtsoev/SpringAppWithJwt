package com.spring.jwt.SpringAppWithJwt.filter;

import com.spring.jwt.SpringAppWithJwt.utils.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
            String token = request.getHeader(AUTHORIZATION);

        if (token == null && !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }



        if (token != null && jwtTokenService.validationToken(token)){
            // Проверка и установка пользователя в сессию через Secutiry context


            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken());
        }
        filterChain.doFilter(request,response);

    }
}
