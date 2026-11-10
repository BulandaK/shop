package com.bulanda.shop.exception.product;

import com.bulanda.shop.exception.ShopException;
import org.springframework.http.HttpStatus;

public class ForbiddenProductNameException extends ShopException {
    public ForbiddenProductNameException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
