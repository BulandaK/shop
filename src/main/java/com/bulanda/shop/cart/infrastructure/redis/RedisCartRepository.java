package com.bulanda.shop.cart.infrastructure.redis;

import com.bulanda.shop.cart.application.port.CartRepository;
import com.bulanda.shop.cart.domain.Cart;
import com.bulanda.shop.cart.domain.CartId;
import com.bulanda.shop.cart.domain.UserId;

import java.util.Optional;

public class RedisCartRepository implements CartRepository {

    @Override
    public void create(Cart cart) {

    }

    @Override
    public Optional<Cart> findById(CartId id) {
        return Optional.empty();
    }

    @Override
    public Optional<CartId> findOpenCartOf(UserId userId) {
        return Optional.empty();
    }

    @Override
    public void addItem(CartId id, Long productId, int quantity) {

    }

    @Override
    public void removeItem(CartId id, Long productId, int quantity) {

    }

    @Override
    public void markCheckedOut(CartId id) {

    }
}
