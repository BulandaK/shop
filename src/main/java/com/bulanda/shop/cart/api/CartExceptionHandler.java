package com.bulanda.shop.cart.api;

import com.bulanda.shop.cart.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CartExceptionHandler {
    @ExceptionHandler(CartException.class)
    ProblemDetail handle(CartException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(statusFor(ex), ex.getMessage());
        problem.setProperty("code", ex.code());
        return problem;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleValidation(IllegalArgumentException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private static HttpStatus statusFor(CartException ex) {
        return switch (ex) {
            case CartNotFoundException e -> HttpStatus.NOT_FOUND;          // 404
            case CartAccessDeniedException e -> HttpStatus.FORBIDDEN;          // 403
            case CartExpiredException e -> HttpStatus.GONE;               // 410
            case CartNotOpenException e -> HttpStatus.CONFLICT;     // 409
            case EmptyCartException e -> HttpStatus.UNPROCESSABLE_ENTITY; // 422
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
