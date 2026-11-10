package com.bulanda.shop.dto.forbiddenphrase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateForbiddenPhraseCommand(
        @NotBlank(message = "Fraza jest wymagana")
        @Size(max = 20, message = "Fraza może mieć maksymalnie 20 znaków")
        String phrase
) {
}
