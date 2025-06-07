package it.volta.ts.productservice.dto;

public record CreateProductRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        boolean available,
        String imageUrl,
        Set<String> categories
) {}