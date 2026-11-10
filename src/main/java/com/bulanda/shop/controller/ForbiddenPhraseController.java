package com.bulanda.shop.controller;

import com.bulanda.shop.dto.forbiddenphrase.CreateForbiddenPhraseCommand;
import com.bulanda.shop.dto.forbiddenphrase.ForbiddenPhraseDTO;
import com.bulanda.shop.service.ForbiddenPhraseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forbidden-phrases")
@RequiredArgsConstructor
public class ForbiddenPhraseController {

    private final ForbiddenPhraseService forbiddenPhraseService;

    @GetMapping
    public List<ForbiddenPhraseDTO> getAll() {
        return forbiddenPhraseService.getAll();
    }

    @GetMapping("/{id}")
    public ForbiddenPhraseDTO get(@PathVariable Long id) {
        return forbiddenPhraseService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ForbiddenPhraseDTO add(@RequestBody @Validated CreateForbiddenPhraseCommand command) {
        return forbiddenPhraseService.add(command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        forbiddenPhraseService.delete(id);
    }
}
