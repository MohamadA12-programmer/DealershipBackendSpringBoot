package com.example.demo.Repositories;

import com.example.demo.entity.Car;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    Page<User> findAll(Pageable pageable);  // default JPA method also supports pagination

}
