package com.example.demo.Repositories;

import com.example.demo.entity.Car;
import com.example.demo.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    Page<Sale> findAll(Pageable pageable);  // default JPA method also supports pagination

}
