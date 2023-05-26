package com.spring.jwt.SpringAppWithJwt.repository;

import com.spring.jwt.SpringAppWithJwt.models.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer,Integer> {
}
