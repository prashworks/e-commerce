package com.ecommerce.ecommerce_starter.repository;

import com.ecommerce.ecommerce_starter.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
