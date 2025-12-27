package com.ecommerce.ecommerce_starter.repository;

import com.ecommerce.ecommerce_starter.model.Cart;
import com.ecommerce.ecommerce_starter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}