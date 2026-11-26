package com.bulanda.shop.cart.domain;

import java.util.Objects;
import java.util.UUID;

public record CartId(UUID value) {
    public CartId {
        Objects.requireNonNull(value,"CartId can't be null");
    }
    public static CartId newId() { return new CartId(UUID.randomUUID()); }
}

