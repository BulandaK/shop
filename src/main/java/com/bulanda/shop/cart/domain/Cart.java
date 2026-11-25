package com.bulanda.shop.cart.domain;

import java.util.List;

public record Cart(CartId id, UserId userId, CartStatus status, List<CartItem> items) { }
