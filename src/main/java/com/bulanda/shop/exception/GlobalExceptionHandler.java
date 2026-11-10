package com.bulanda.shop.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.InvalidFormatException;
import tools.jackson.databind.exc.MismatchedInputException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ShopException.class)
    public ProblemDetail handleShopException(ShopException ex) {
        return ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.putIfAbsent(e.getField(), e.getDefaultMessage()));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Niepoprawne dane wejściowe");
        problem.setProperty("errors", errors);

        return ResponseEntity.badRequest().body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problem;
        if (ex.getCause() instanceof MismatchedInputException mie && !mie.getPath().isEmpty()) {
            String field = fieldPath(mie);
            problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Niepoprawne dane wejściowe");
            problem.setProperty("errors", Map.of(field, describeMismatch(mie)));
        } else {
            problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Niepoprawny format JSON");
        }
        return ResponseEntity.badRequest().body(problem);
    }

    private static String describeMismatch(MismatchedInputException ex) {
        Class<?> target = ex.getTargetType();
        if (target != null && target.isEnum()) {
            String allowed = Arrays.stream(target.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            Object value = ex instanceof InvalidFormatException ife ? ife.getValue() : null;
            return value != null
                    ? "Niepoprawna wartość '" + value + "'. Dozwolone: " + allowed
                    : "Niepoprawna wartość. Dozwolone: " + allowed;
        }
        if (ex instanceof InvalidFormatException ife) {
            return "Niepoprawna wartość '" + ife.getValue() + "'";
        }
        return "Niepoprawny typ danych";
    }

    private static String fieldPath(MismatchedInputException ex) {
        StringBuilder sb = new StringBuilder();
        for (JacksonException.Reference ref : ex.getPath()) {
            if (ref.getPropertyName() != null) {
                if (!sb.isEmpty()) sb.append('.');
                sb.append(ref.getPropertyName());
            } else if (ref.getIndex() >= 0) {
                sb.append('[').append(ref.getIndex()).append(']');
            }
        }
        return sb.toString();
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }
}
