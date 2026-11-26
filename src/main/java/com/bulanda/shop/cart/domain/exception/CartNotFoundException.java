package com.bulanda.shop.cart.domain.exception;

import com.bulanda.shop.cart.domain.CartId;

public class CartNotFoundException extends CartException{
    public CartNotFoundException(CartId cartId) {
        super("CART_NOT_FOUND", "Cart not found exception %s".formatted(cartId));
    }
}
