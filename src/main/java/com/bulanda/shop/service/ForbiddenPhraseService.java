package com.bulanda.shop.service;

import com.bulanda.shop.dto.forbiddenphrase.CreateForbiddenPhraseCommand;
import com.bulanda.shop.dto.forbiddenphrase.ForbiddenPhraseDTO;
import com.bulanda.shop.exception.forbiddenphrase.ForbiddenPhraseAlreadyExistsException;
import com.bulanda.shop.exception.forbiddenphrase.ForbiddenPhraseNotFoundException;
import com.bulanda.shop.exception.product.ForbiddenProductNameException;
import com.bulanda.shop.mapper.ForbiddenPhraseMapper;
import com.bulanda.shop.model.ForbiddenPhrase;
import com.bulanda.shop.repository.ForbiddenPhraseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ForbiddenPhraseService {

    private final ForbiddenPhraseRepository forbiddenPhraseRepository;
    private final ForbiddenPhraseMapper forbiddenPhraseMapper;

    public List<ForbiddenPhraseDTO> getAll() {
        return forbiddenPhraseMapper.toDtoList(forbiddenPhraseRepository.findAll());
    }

    public ForbiddenPhraseDTO get(Long id) {
        return forbiddenPhraseMapper.toDto(findOrThrow(id));
    }

    @Transactional
    public ForbiddenPhraseDTO add(CreateForbiddenPhraseCommand command) {
        String phrase = normalize(command.phrase());
        if (forbiddenPhraseRepository.existsByPhrase(phrase)) {
            throw new ForbiddenPhraseAlreadyExistsException("Taka zabroniona fraza już istnieje");
        }
        ForbiddenPhrase saved = forbiddenPhraseRepository.save(new ForbiddenPhrase(null, phrase));
        return forbiddenPhraseMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        forbiddenPhraseRepository.delete(findOrThrow(id));
    }

    public void assertNameAllowed(String name) {
        List<String> matched = forbiddenPhraseRepository.findPhrasesContainedIn(normalize(name));
        if (!matched.isEmpty()) {
            throw new ForbiddenProductNameException(
                    "Nazwa produktu zawiera zabronione frazy: " + String.join(", ", matched));
        }
    }

    private ForbiddenPhrase findOrThrow(Long id) {
        return forbiddenPhraseRepository.findById(id)
                .orElseThrow(() -> new ForbiddenPhraseNotFoundException("Zabroniona fraza o takim id nie istnieje"));
    }

    private static String normalize(String text) {
        return text.strip().toLowerCase(Locale.ROOT);
    }
}
