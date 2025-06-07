package it.volta.ts.productservice.dto;

import java.math.BigDecimal;
import java.util.Set;

public record UpdateProductRequest(
        String name,
        String description,
        BigDecimal price,
        Boolean available,
        Set<String> categories
) {}