package it.volta.ts.cartservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Cart {
    private List<CartItem> items = new ArrayList<>();
    private Instant lastUpdated;
}