package com.bulanda.shop.cart.domain.exception;

import com.bulanda.shop.cart.domain.CartId;

public class CartExpiredException extends CartException {
    public CartExpiredException(CartId cartId) {
        super("CART_EXPIRED", "Koszyk %s wygasł".formatted(cartId));
    }
}
