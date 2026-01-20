package com.sroyc.rex.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sroyc.rex.example.entity.OrderMaster;
import com.sroyc.rex.example.repository.OrderMasterRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @GetMapping("/stream")
    public Flux<OrderMaster> getAllOrders(@RequestParam(value = "limit", required = false) int limit) {
        if (limit > 0) {
            return orderMasterRepository.findAll().take(limit);
        }
        return orderMasterRepository.findAll();
    }

    @GetMapping("/id/{id}")
    public Mono<OrderMaster> getOrderById(@PathVariable("id") Long id) {
        return orderMasterRepository.findById(id);
    }

    @GetMapping("/user/{userId}")
    public Flux<OrderMaster> getOrdersByUserId(@PathVariable("userId") Long userId) {
        return orderMasterRepository.findAll().filter(order -> order.getUserId().equals(userId));
    }

}
