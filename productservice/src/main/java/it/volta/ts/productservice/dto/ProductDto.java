package it.volta.ts.productservice.dto;

import java.math.BigDecimal;
import java.util.Set;

public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean available,
        String imageUrl,
        Set<String> categories
) {}