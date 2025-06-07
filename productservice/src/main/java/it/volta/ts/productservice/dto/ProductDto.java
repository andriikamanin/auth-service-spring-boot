package it.volta.ts.productservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean available,
        List<String> imageUrls, // ✅ заменили imageUrl на список
        Set<String> categories
) {}