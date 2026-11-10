package com.bulanda.shop.exception.product;

import com.bulanda.shop.exception.ShopException;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends ShopException {
    public ProductNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND,message);
    }
}
