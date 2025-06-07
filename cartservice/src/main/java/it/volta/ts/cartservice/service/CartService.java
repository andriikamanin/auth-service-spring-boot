package it.volta.ts.cartservice.service;

import it.volta.ts.cartservice.dto.AddItemRequest;
import it.volta.ts.cartservice.dto.UpdateItemRequest;
import it.volta.ts.cartservice.entity.Cart;
import it.volta.ts.cartservice.entity.CartItem;
import it.volta.ts.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final S3ImageUrlService s3ImageUrlService;

    public Cart getCart(UUID userId) {
        Cart cart = cartRepository.getCart(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setLastUpdated(Instant.now());
        }
        return cart;
    }

    public Cart addItem(UUID userId, AddItemRequest request) {
        Cart cart = getCart(userId);

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setCount(item.getCount() + request.getCount());
        } else {
            CartItem newItem = new CartItem(
                    request.getProductId(),
                    request.getCount(),
                    request.getPriceSnapshot(),
                    request.getTitleSnapshot(),
                    request.getImageUrl(),   // ✅ напрямую из DTO
                    request.getSize()
            );
            cart.getItems().add(newItem);
        }

        cart.setLastUpdated(Instant.now());
        cartRepository.saveCart(userId, cart);
        return cart;
    }

    public Cart updateItem(UUID userId, Long productId, UpdateItemRequest request) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));

        if (request.getCount() > 0) {
            String imageUrl = s3ImageUrlService.buildUrl(request.getImageKey());

            CartItem newItem = new CartItem(
                    productId,
                    request.getCount(),
                    request.getPriceSnapshot(),
                    request.getTitleSnapshot(),
                    imageUrl,
                    request.getSize()
            );
            cart.getItems().add(newItem);
        }

        cart.setLastUpdated(Instant.now());
        cartRepository.saveCart(userId, cart);
        return cart;
    }

    public Cart deleteItem(UUID userId, Long productId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        cart.setLastUpdated(Instant.now());
        cartRepository.saveCart(userId, cart);
        return cart;
    }

    public void clearCart(UUID userId) {
        cartRepository.deleteCart(userId);
    }
}