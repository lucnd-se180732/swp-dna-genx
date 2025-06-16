package com.genx.dto;


import java.math.BigDecimal;

public class ServiceTypeDto {
    public record ServiceTypeDTO(
            Long typeId,
            String typeName,
            BigDecimal price
    ) {}
}
