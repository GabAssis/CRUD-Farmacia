package com.gabriel.drugstore.repository;

import com.gabriel.drugstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByExpirationBefore(@Param("expiration")LocalDate expiration);

    List<Product> findAllByNameContainingIgnoreCase(@Param("name")String name);
}
