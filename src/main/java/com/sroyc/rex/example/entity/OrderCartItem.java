package com.sroyc.rex.example.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ORDER_CART_ITEM")
public class OrderCartItem {

    @Id
    private Long orderItemId;

    private Long orderId;

    private Long productId;

    private BigDecimal price;

    private Integer quantity;

    @CreatedDate
    private LocalDateTime createdOn;

}
