package com.genx.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CollectionOptionResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long serviceTypeId;
    private String serviceTypeName;

    // Getters and setters
}