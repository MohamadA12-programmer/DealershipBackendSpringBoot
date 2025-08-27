package com.example.demo.Repositories;

import com.example.demo.entity.Car;
import com.example.demo.entity.SalesPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalespersonRepository extends JpaRepository<SalesPerson, Integer> {
    Page<SalesPerson> findAll(Pageable pageable);  // default JPA method also supports pagination

}
