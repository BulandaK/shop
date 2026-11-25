package com.bulanda.shop.cart.domain;

import java.util.UUID;

public record CartId(UUID value) {
    public static CartId newId() { return new CartId(UUID.randomUUID()); }
}

