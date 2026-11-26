package com.bulanda.shop.cart.domain.event;

import com.bulanda.shop.cart.domain.CartId;
import com.bulanda.shop.cart.domain.ProductId;

import java.time.Instant;

public record ItemAdded(CartId cartId, ProductId productId, int quantity, Instant newExpiresAt) implements CartEvent {}
