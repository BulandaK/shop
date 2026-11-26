package com.bulanda.shop.cart.domain.exception;

import com.bulanda.shop.cart.domain.CartId;

public class CartNotOpenException extends CartException {
    public CartNotOpenException(CartId cartId) {
        super("CART_NOT_OPEN", "Koszyk %s został już zatwierdzony".formatted(cartId));
    }
}
