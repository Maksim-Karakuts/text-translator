package com.tinkoff.maksim.karakuts.text.translator;

import com.tinkoff.maksim.karakuts.text.translator.dto.InputText;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslatedText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@Service
public class TranslationServiceImpl implements TranslationService {
    private static final String WORD_DELIMITER = " ";
    private static final Logger LOGGER =
        LoggerFactory.getLogger(TranslationServiceImpl.class);
    private final ExternalTranslator translator;

    @Autowired
    public TranslationServiceImpl(ExternalTranslator externalTranslator) {
        this.translator = externalTranslator;
    }

    @Override
    public TranslatedText translate(InputText text) {
        LOGGER.debug("Translating text '{}'", text);
        TranslatedText translatedText = new TranslatedText();
        String initialText = text.getText();
        String[] textWords = initialText.split(WORD_DELIMITER);
        String[] translatedWords =
            Arrays.stream(textWords).map(word -> translator.translateWord(word,
                    text.getInitialLanguage(), text.getTargetLanguage()))
                .map(future -> {
                    String result = null;
                    try {
                        result = future.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new ExternalApiException(
                            "Getting translation from external API was " +
                                "interrupted", e);
                    } catch (ExecutionException e) {
                        throw new ExternalApiException(
                            "Can't get word translation from external API", e);
                    }
                    return result;
                }).toArray(String[]::new);
        translatedText.setText(String.join(WORD_DELIMITER, translatedWords));
        LOGGER.debug("Translation finished with {}", translatedText);
        return translatedText;
    }
}
