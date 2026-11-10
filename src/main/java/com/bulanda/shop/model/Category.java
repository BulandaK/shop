package com.bulanda.shop.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public enum Category {
    ELECTRONICS(new BigDecimal("50"), new BigDecimal("50000")),
    BOOKS(new BigDecimal("5"),new BigDecimal("500")),
    CLOTHES(new BigDecimal("10"), new BigDecimal("5000"));

    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;

    public boolean isPriceAllowed(BigDecimal price) {
    return price.compareTo(minPrice) >= 0 && price.compareTo(maxPrice) <=0;
    }
}
