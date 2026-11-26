package com.bulanda.shop.cart.domain.exception;

import com.bulanda.shop.cart.domain.CartId;

public class CartAccessDeniedException extends CartException {

    protected CartAccessDeniedException(CartId cartId) {
        super("CART_ACCESS_DENIED", "Acces for Cart denied");
    }
}
