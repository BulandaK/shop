package com.bulanda.shop.cart.domain.exception;

public abstract class CartException extends RuntimeException {

    private final String code;

    protected CartException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String code() { return code; }
}