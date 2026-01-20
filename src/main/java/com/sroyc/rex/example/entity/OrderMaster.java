package com.sroyc.rex.example.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.sroyc.rex.example.common.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ORDER_MASTER")
public class OrderMaster {

    @Id
    private Long orderId;

    private Long userId;

    private BigDecimal totalPrice;

    private OrderStatus status;

    private String remarks;

}
