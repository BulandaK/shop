package com.bulanda.shop.dto.product;

import com.bulanda.shop.model.Category;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateProductCommand(
        @NotBlank(message = "Nazwa jest wymagana")
        @Size(min = 3, max = 20, message = "Nazwa musi mieć od 3 do 20 znaków")
        @Pattern(regexp = "^[\\p{L}0-9]+( [\\p{L}0-9]+)*$",
                message = "Nazwa może zawierać tylko litery, cyfry i pojedyncze spacje")
        String name,

        @NotNull(message = "Cena jest wymagana")
        @Positive(message = "Cena musi być większa od zera")
        BigDecimal price,

        @NotNull(message = "Kategoria jest wymagana")
        Category category,

        @NotNull(message = "Ilość jest wymagana")
        @PositiveOrZero(message = "Ilość nie może być ujemna")
        Long availableCount
) {
}
