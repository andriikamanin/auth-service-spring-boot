package it.volta.ts.cartservice.controller;

import it.volta.ts.cartservice.dto.AddItemRequest;
import it.volta.ts.cartservice.dto.UpdateItemRequest;
import it.volta.ts.cartservice.entity.Cart;
import it.volta.ts.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private UUID getUserId() {
        return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping
    public Cart getCart() {
        return cartService.getCart(getUserId());
    }

    @PostMapping("/items")
    public Cart addItem(@RequestBody AddItemRequest request) {
        return cartService.addItem(getUserId(), request);
    }

    @PutMapping("/items/{productId}")
    public Cart updateItem(@PathVariable Long productId,
                           @RequestBody UpdateItemRequest request) {
        return cartService.updateItem(getUserId(), productId, request);
    }

    @DeleteMapping("/items/{productId}")
    public Cart deleteItem(@PathVariable Long productId) {
        return cartService.deleteItem(getUserId(), productId);
    }

    @DeleteMapping
    public void clearCart() {
        cartService.clearCart(getUserId());
    }
}