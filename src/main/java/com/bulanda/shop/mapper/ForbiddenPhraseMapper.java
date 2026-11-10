package com.bulanda.shop.mapper;

import com.bulanda.shop.dto.forbiddenphrase.ForbiddenPhraseDTO;
import com.bulanda.shop.model.ForbiddenPhrase;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ForbiddenPhraseMapper {

    ForbiddenPhraseDTO toDto(ForbiddenPhrase forbiddenPhrase);

    List<ForbiddenPhraseDTO> toDtoList(List<ForbiddenPhrase> forbiddenPhrases);
}
