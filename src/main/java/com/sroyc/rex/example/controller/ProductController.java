package com.sroyc.rex.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Flux<Product> getProductByName(@RequestParam("name") String name) {
        return productRepository.findAll().filter(product -> product.getName().contains(name));
    }

}
