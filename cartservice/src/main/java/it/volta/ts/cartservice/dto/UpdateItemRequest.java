package it.volta.ts.cartservice.dto;

import lombok.Data;

@Data
public class UpdateItemRequest {
    private int count;
    private int priceSnapshot;
    private String titleSnapshot;
    private String imageKey;  // ✅ путь или ключ в S3 (если строишь ссылку вручную)
    private String size;      // ✅ размер
}