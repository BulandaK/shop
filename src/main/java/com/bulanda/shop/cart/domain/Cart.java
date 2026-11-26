package com.bulanda.shop.cart.domain;

import com.bulanda.shop.cart.domain.event.CartCheckedOut;
import com.bulanda.shop.cart.domain.event.CartCreated;
import com.bulanda.shop.cart.domain.event.ItemAdded;
import com.bulanda.shop.cart.domain.event.ItemRemoved;
import com.bulanda.shop.cart.domain.exception.CartExpiredException;
import com.bulanda.shop.cart.domain.exception.CartNotOpenException;
import com.bulanda.shop.cart.domain.exception.EmptyCartException;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {

    public static final Duration TTL = Duration.ofMinutes(15);

    private final CartId id;
    private final UserId userId;
    private final Instant createdAt;
    private CartStatus status;
    private Instant expiresAt;
    private final Map<ProductId, Integer> items;

    public Cart(CartId id, UserId userId, CartStatus status,
                Instant createdAt, Instant expiresAt, Map<ProductId, Integer> items) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.items = new HashMap<>(items);
    }

    public static CartCreated create(UserId userId, Instant now) {
        return new CartCreated(CartId.newId(),userId,now,now.plus(TTL));
    }

    public ItemAdded addItem(ProductId productId, int quantity, Instant now){
        requirePositive(quantity);
        ensureModifiable(now);
        items.merge(productId, quantity, Integer::sum);
        expiresAt = now.plus(TTL);                 // sliding expiration
        return new ItemAdded(id, productId, quantity, expiresAt);
    }

    public ItemRemoved removeItem(ProductId productId, int quantity, Instant now) {
        requirePositive(quantity);
        ensureModifiable(now);
        items.computeIfPresent(productId, (p, q) -> q - quantity > 0 ? q - quantity : null);
        expiresAt = now.plus(TTL);
        return new ItemRemoved(id, productId, quantity, expiresAt);
    }

    public CartCheckedOut checkout(Instant now) {
        ensureModifiable(now);
        if (items.isEmpty()) throw new EmptyCartException(id);
        status = CartStatus.CHECKED_OUT;
        return new CartCheckedOut(id, userId, Map.copyOf(items), now);
    }

    private void ensureModifiable(Instant now) {
        if (status != CartStatus.OPEN) throw new CartNotOpenException(id);
        if (isExpired(now)) throw new CartExpiredException(id);
    }

    private static void requirePositive(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Ilość musi być > 0");
    }

    public boolean isExpired(Instant now) { return !now.isBefore(expiresAt); }
}
