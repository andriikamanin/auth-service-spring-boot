package it.volta.ts.cartservice.repository;

import it.volta.ts.cartservice.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final RedisTemplate<String, Cart> redisTemplate;

    private static final Duration CART_TTL = Duration.ofDays(7);

    private String getKey(UUID userId) {
        return "cart:" + userId.toString();
    }

    public Cart getCart(UUID userId) {
        return redisTemplate.opsForValue().get(getKey(userId));
    }

    public void saveCart(UUID userId, Cart cart) {
        redisTemplate.opsForValue().set(getKey(userId), cart, CART_TTL.getSeconds(), TimeUnit.SECONDS);
    }

    public void deleteCart(UUID userId) {
        redisTemplate.delete(getKey(userId));
    }
}