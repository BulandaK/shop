package com.bulanda.shop.cart.domain.exception;

import com.bulanda.shop.cart.domain.CartId;

public class CartExpiredExcetpion extends CartException {
    public CartExpiredExcetpion(CartId cartId) {
        super("CART_EXPIRED", "Koszyk %s wygasł".formatted(cartId));
    }
}
