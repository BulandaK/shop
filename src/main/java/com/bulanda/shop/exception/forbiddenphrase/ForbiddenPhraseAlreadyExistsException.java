package com.bulanda.shop.exception.forbiddenphrase;

import com.bulanda.shop.exception.ShopException;
import org.springframework.http.HttpStatus;

public class ForbiddenPhraseAlreadyExistsException extends ShopException {
    public ForbiddenPhraseAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
