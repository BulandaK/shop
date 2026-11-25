package com.bulanda.shop.cart.application.port;

import com.bulanda.shop.cart.domain.Cart;
import com.bulanda.shop.cart.domain.CartId;
import com.bulanda.shop.cart.domain.UserId;

import java.util.Optional;

public interface CartRepository {
    void create(Cart cart);
    Optional<Cart> findById(CartId id);
    Optional<CartId> findOpenCartOf(UserId userId);
    void addItem(CartId id, Long productId, int quantity);
    void removeItem(CartId id, Long productId, int quantity);
    void markCheckedOut(CartId id);
}
