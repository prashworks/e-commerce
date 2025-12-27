package com.ecommerce.ecommerce_starter.controller;

import com.ecommerce.ecommerce_starter.model.Product;
import com.ecommerce.ecommerce_starter.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepo;

    public ProductController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }




    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "products";
    }


    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "product_form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String saveProduct(@ModelAttribute Product product) {
        productRepo.save(product);
        return "redirect:/products";
    }
}