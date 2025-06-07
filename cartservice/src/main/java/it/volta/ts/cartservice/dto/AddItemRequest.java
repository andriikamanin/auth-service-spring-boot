package it.volta.ts.cartservice.dto;

import lombok.Data;

@Data
public class AddItemRequest {
    private Long productId;
    private int count;
    private String titleSnapshot;
    private int priceSnapshot; // В центах
    private String imageUrl;   // ✅
    private String size;       // ✅
}