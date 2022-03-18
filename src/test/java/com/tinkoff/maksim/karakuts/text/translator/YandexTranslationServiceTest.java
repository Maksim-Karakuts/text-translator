package com.tinkoff.maksim.karakuts.text.translator;

import com.tinkoff.maksim.karakuts.text.translator.dto.InputText;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslatedText;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YandexTranslationServiceTest {
    private TranslationService translationService =
        new YandexTranslationService();

    @Test
    void translate_whenTwoWords_thenReturnTwoTranslatedWords() {
        InputText inputText = new InputText("start end", "EN", "RU");
        TranslatedText expectedTranslatedText =
            new TranslatedText("EN[start]RU EN[end]RU");

        TranslatedText actualTranslatedText =
            translationService.translate(inputText);

        assertEquals(expectedTranslatedText, actualTranslatedText);
    }
}
