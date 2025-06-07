// CartItemSnapshot.java
package it.volta.ts.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemSnapshot {
    private Long productId;
    private String title;
    private Integer price;
    private String imageUrl;
    private String size;
}