package com.ecommerce.ecommerce_starter.controller;

import com.ecommerce.ecommerce_starter.model.Cart;
import com.ecommerce.ecommerce_starter.model.Product;
import com.ecommerce.ecommerce_starter.model.User;
import com.ecommerce.ecommerce_starter.repository.CartRepository;
import com.ecommerce.ecommerce_starter.repository.ProductRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public CartController(CartRepository cartRepo, ProductRepository productRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    @GetMapping
    public String viewCart(@AuthenticationPrincipal User user, Model model) {
        Cart cart = cartRepo.findByUser(user).orElse(new Cart(user));
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> new Cart(user));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cart.addProduct(product);
        cartRepo.save(cart);

        return "redirect:/cart";
    }
}