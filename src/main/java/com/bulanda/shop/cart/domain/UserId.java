package com.bulanda.shop.cart.domain;


import java.util.Objects;

public record UserId(String value) {

    public UserId{
        Objects.requireNonNull(value, "UserId cant be null");
    }
}
