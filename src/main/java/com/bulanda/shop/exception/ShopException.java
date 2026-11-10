package com.bulanda.shop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ShopException extends RuntimeException {

    private final HttpStatus status;

    protected ShopException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    protected ShopException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
