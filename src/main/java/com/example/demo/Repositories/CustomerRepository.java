package com.example.demo.Repositories;

import com.example.demo.entity.Car;
import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Page<Customer> findAll(Pageable pageable);  // default JPA method also supports pagination

}
