package com.bulanda.shop.repository;

import com.bulanda.shop.model.ForbiddenPhrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ForbiddenPhraseRepository extends JpaRepository<ForbiddenPhrase, Long> {

    boolean existsByPhrase(String phrase);

    /**
     * Zwraca zabronione frazy zawarte w podanym tekście.
     * Tekst musi być już znormalizowany (małe litery). LOCATE zamiast LIKE,
     * żeby znaki '%' i '_' we frazie nie działały jak wildcardy.
     */
    @Query("select f.phrase from ForbiddenPhrase f where locate(f.phrase, :text) > 0")
    List<String> findPhrasesContainedIn(@Param("text") String text);
}
