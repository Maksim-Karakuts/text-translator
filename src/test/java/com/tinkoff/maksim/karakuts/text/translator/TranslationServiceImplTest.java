package com.tinkoff.maksim.karakuts.text.translator;

import com.tinkoff.maksim.karakuts.text.translator.dto.InputText;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslatedText;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TranslationServiceImplTest {
    @Mock
    private ExternalTranslator translator;
    @InjectMocks
    private TranslationServiceImpl translationService;
    private AutoCloseable closeable;

    @BeforeEach
    void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void translate_whenTwoWords_thenReturnTwoTranslatedWords() {
        InputText inputText = new InputText("start end", "en", "ru");
        TranslatedText expectedTranslatedText =
            new TranslatedText("начало конец");

        doReturn(CompletableFuture.completedFuture("начало")).when(translator)
            .translateWord("start", "en", "ru");
        doReturn(CompletableFuture.completedFuture("конец")).when(translator)
            .translateWord("end", "en", "ru");

        TranslatedText actualTranslatedText =
            translationService.translate(inputText);

        assertEquals(expectedTranslatedText, actualTranslatedText);
    }
}
