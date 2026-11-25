package com.bulanda.shop.cart.application.command;

import com.bulanda.shop.cart.application.port.CartRepository;
import com.bulanda.shop.cart.domain.CartId;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCartHandler {
    private final CartRepository cartRepository;

    public CartId handle(CreateCartHandler cmomand) {

    }
}
