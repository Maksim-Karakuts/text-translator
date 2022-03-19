package com.tinkoff.maksim.karakuts.text.translator.repository;

import com.tinkoff.maksim.karakuts.text.translator.dto.Translation;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslationData;

public interface TranslationRepository {
    Translation add(TranslationData translationData);
}
