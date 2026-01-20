package com.sroyc.rex.example.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.sroyc.rex.example.common.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("PRODUCTS")
public class Product {

    @Id
    private Long id;

    private String code;

    private String name;

    private String description;

    private Category category;

    private Integer quantity;

    private java.math.BigDecimal price;
}
