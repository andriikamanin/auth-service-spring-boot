package it.volta.ts.cartservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    Long productId;
    int count;
    int priceSnapshot;
    String titleSnapshot;
    String imageUrl;  // ✅
    String size;      // ✅
}