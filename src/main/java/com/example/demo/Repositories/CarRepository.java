package com.example.demo.Repositories;

import com.example.demo.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Integer> {
    Page<Car> findAll(Pageable pageable);  // default JPA method also supports pagination
}
