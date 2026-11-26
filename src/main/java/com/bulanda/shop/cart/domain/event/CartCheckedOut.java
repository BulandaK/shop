package com.bulanda.shop.cart.domain.event;

import com.bulanda.shop.cart.domain.CartId;
import com.bulanda.shop.cart.domain.ProductId;
import com.bulanda.shop.cart.domain.UserId;

import java.time.Instant;
import java.util.Map;

public record CartCheckedOut(CartId cartId, UserId userId, Map<ProductId, Integer> items, Instant at) implements CartEvent {}
