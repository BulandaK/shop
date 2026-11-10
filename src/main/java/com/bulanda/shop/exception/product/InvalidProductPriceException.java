package com.bulanda.shop.exception.product;

import com.bulanda.shop.exception.ShopException;
import org.springframework.http.HttpStatus;

public class InvalidProductPriceException extends ShopException {
    public InvalidProductPriceException( String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
