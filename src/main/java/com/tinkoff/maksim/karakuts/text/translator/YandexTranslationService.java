package com.tinkoff.maksim.karakuts.text.translator;

import com.tinkoff.maksim.karakuts.text.translator.dto.InputText;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslatedText;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class YandexTranslationService implements TranslationService {
    private static final String WORD_DELIMITER = " ";

    @Override
    public TranslatedText translate(InputText text) {
        TranslatedText translatedText = new TranslatedText();
        String initialText = text.getText();
        String[] textWords = initialText.split(WORD_DELIMITER);
        translatedText.setText(Arrays.stream(textWords).map(
                word -> translateWord(word, text.getInitialLanguage(),
                    text.getTargetLanguage()))
            .collect(Collectors.joining(WORD_DELIMITER)));
        return translatedText;
    }

    private String translateWord(String word, String initialLanguage,
                                 String targetLanguage) {
        return initialLanguage + "[" + word + "]" + targetLanguage;
    }
}
