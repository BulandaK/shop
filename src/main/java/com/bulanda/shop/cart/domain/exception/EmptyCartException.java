package com.bulanda.shop.cart.domain.exception;

import com.bulanda.shop.cart.domain.CartId;

public class EmptyCartException extends CartException {
    public EmptyCartException(CartId cartId) {
        super("CART_EMPTY", "Nie można zatwierdzić pustego koszyka %s".formatted(cartId));
    }
}
