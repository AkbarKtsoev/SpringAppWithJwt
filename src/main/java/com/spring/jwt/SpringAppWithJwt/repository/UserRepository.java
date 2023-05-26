package com.spring.jwt.SpringAppWithJwt.repository;

import com.spring.jwt.SpringAppWithJwt.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<Employee,Integer> {
    


    Optional<Employee> findByUsername(String username);
}
