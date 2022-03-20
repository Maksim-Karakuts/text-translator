package com.tinkoff.maksim.karakuts.text.translator;

import com.tinkoff.maksim.karakuts.text.translator.dto.InputText;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslatedText;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String initialText = text.getText();
        List<String> initialWords =
            Arrays.asList(initialText.split(WORD_DELIMITER));

        List<String> resultWords =
            translateWords(initialWords, text.getInitialLanguage(),
                text.getTargetLanguage());
        String resultText = String.join(WORD_DELIMITER,
            resultWords);

        TranslatedText translatedText =
            initTranslatedText(text, initialWords, resultText, resultWords);
        LOGGER.debug("Translation finished with {}", translatedText);
        return translatedText;
    }

    private List<String> translateWords(List<String> initialWords,
                                        String initialLanguage,
                                        String targetLanguage) {
        LOGGER.debug("Translating words {}", initialWords);
        List<CompletableFuture<String>> translatedWordFutures =
            initialWords.stream().map(word -> translator.translateWord(word,
                initialLanguage, targetLanguage)).collect(Collectors.toList());
        translatedWordFutures.forEach(CompletableFuture::join);

        List<String> translatedWords =
            translatedWordFutures.stream().map(future -> {
                try {
                    return future.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ExternalApiException(
                        "Getting translation from external API was " +
                            "interrupted", e);
                } catch (ExecutionException e) {
                    throw new ExternalApiException(
                        "Can't get word translation from external API", e);
                }
            }).collect(Collectors.toList());

        LOGGER.debug("Translated words {}", translatedWords);
        return translatedWords;
    }

    private TranslatedText initTranslatedText(InputText inputText,
                                              List<String> initialWords,
                                              String translationText,
                                              List<String> translatedWords) {
        TranslatedText translatedText = new TranslatedText();
        translatedText.setInitialText(inputText.getText());
        translatedText.setInitialWords(initialWords);
        translatedText.setInitialLanguage(inputText.getInitialLanguage());
        translatedText.setTargetLanguage(inputText.getTargetLanguage());
        translatedText.setResultText(translationText);
        translatedText.setResultWords(translatedWords);
        return translatedText;
    }
}
