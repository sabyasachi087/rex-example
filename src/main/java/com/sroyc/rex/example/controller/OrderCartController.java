package com.sroyc.rex.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sroyc.rex.example.entity.OrderCartItem;
import com.sroyc.rex.example.repository.OrderCartItemRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cart")
public class OrderCartController {

    @Autowired
    private OrderCartItemRepository orderCartItemRepository;

    @GetMapping("/stream")
    public Flux<OrderCartItem> getAllOrderCarts() {
        return orderCartItemRepository.findAll();
    }

    @GetMapping("/id/{id}")
    public Mono<OrderCartItem> getOrderCartById(@PathVariable("id") Long id) {
        return orderCartItemRepository.findById(id);
    }

    @GetMapping("/order/{orderId}")
    public Flux<OrderCartItem> getOrderCartsByOrderId(@PathVariable("orderId") Long orderId) {
        return orderCartItemRepository.findAll().filter(orderCartItem -> orderCartItem.getOrderId().equals(orderId));
    }

}
