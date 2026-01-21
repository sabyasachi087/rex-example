package com.sroyc.rex.example.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sroyc.rex.example.common.Category;
import com.sroyc.rex.example.entity.Product;
import com.sroyc.rex.example.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/stream")
    public Flux<Product> getAllProducts(@RequestParam(value = "limit", required = false) int limit) {
        if (limit > 0) {
            return productRepository.findAll().take(limit);
        }
        return productRepository.findAll();
    }

    @GetMapping("/id/{id}")
    public Mono<Product> getProductById(@PathVariable("id") Long id) {
        return productRepository.findById(id);
    }

    @GetMapping("/search")
    public Flux<Product> getProductByName(@RequestParam(value = "name", required = false) Optional<String> name,
            @RequestParam(value = "category", required = false) Optional<Category> cat) {
        return productRepository.findAll()
                .filter(product -> {
                    if (name.isPresent() && cat.isPresent()) {
                        return product.getName().toLowerCase().contains(name.get().toLowerCase())
                                && product.getCategory().equals(cat.get());
                    } else if (name.isPresent()) {
                        return product.getName().toLowerCase().contains(name.get().toLowerCase());
                    } else if (cat.isPresent()) {
                        return product.getCategory().equals(cat.get());
                    }
                    return false;
                });
    }

}
