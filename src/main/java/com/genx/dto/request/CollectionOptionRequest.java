package com.genx.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

@Getter
@Setter
public class CollectionOptionRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Long serviceTypeId;

    // Getters and setters
}