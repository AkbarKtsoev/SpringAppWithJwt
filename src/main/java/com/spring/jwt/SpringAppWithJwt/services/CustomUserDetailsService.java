package com.spring.jwt.SpringAppWithJwt.services;

import com.spring.jwt.SpringAppWithJwt.exceptionObjects.UserNotFoundException;
import com.spring.jwt.SpringAppWithJwt.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    @Autowired
    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  employeeRepository.findByUsername(username)
                .orElseThrow(() ->new UserNotFoundException("Username not found exception"));

    }
}
