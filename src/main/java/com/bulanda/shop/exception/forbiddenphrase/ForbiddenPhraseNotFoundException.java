package com.bulanda.shop.exception.forbiddenphrase;

import com.bulanda.shop.exception.ShopException;
import org.springframework.http.HttpStatus;

public class ForbiddenPhraseNotFoundException extends ShopException {
    public ForbiddenPhraseNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
