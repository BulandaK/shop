package com.bulanda.shop.cart.domain.event;

import com.bulanda.shop.cart.domain.CartId;
import com.bulanda.shop.cart.domain.ProductId;
import com.bulanda.shop.cart.domain.UserId;

import java.time.Instant;
import java.util.Map;

public record CartCreated(CartId cartId, UserId userId, Instant createdAt, Instant expiresAt) implements CartEvent {}

