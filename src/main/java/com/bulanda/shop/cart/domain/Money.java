package com.bulanda.shop.cart.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount) {
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        Objects.requireNonNull(amount);
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
    public Money add(Money other)   { return new Money(amount.add(other.amount)); }
    public Money times(int quantity) { return new Money(amount.multiply(BigDecimal.valueOf(quantity))); }
}
