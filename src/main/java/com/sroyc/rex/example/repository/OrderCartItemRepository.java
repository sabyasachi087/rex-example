package com.sroyc.rex.example.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.sroyc.rex.example.entity.OrderCartItem;

@Repository
public interface OrderCartItemRepository extends ReactiveCrudRepository<OrderCartItem, Long> {

}
