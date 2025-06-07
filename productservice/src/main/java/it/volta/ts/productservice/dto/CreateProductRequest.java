package it.volta.ts.productservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

public record CreateProductRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        boolean available,
        Set<String> categories
) {}