package com.bulanda.shop.exception.product;

import com.bulanda.shop.exception.ShopException;
import org.springframework.http.HttpStatus;

public class ProductAlreadyExistsException extends ShopException {
    public ProductAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT,message);
    }
}
