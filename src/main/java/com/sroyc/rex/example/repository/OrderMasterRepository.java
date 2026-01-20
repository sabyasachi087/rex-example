package com.sroyc.rex.example.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.sroyc.rex.example.entity.OrderMaster;

@Repository
public interface OrderMasterRepository extends ReactiveCrudRepository<OrderMaster, Long> {

}
