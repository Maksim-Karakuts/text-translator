package com.tinkoff.maksim.karakuts.text.translator;

import com.tinkoff.maksim.karakuts.text.translator.dto.InputText;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslatedText;
import com.tinkoff.maksim.karakuts.text.translator.dto.Translation;
import com.tinkoff.maksim.karakuts.text.translator.repository.TranslationRepository;
import com.tinkoff.maksim.karakuts.text.translator.service.TranslationService;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TranslationController.class)
class TranslationControllerTest {
    private static final long TRANSLATION_ID = 1L;
    private static final String INITIAL_TEXT = "start end";
    private static final List<String> INITIAL_WORDS =
        Arrays.asList("start", "end");
    private static final String INITIAL_LANGUAGE = "en";
    private static final String TARGET_LANGUAGE = "ru";
    private static final String TRANSLATED_TEXT = "начало конец";
    private static final List<String> TRANSLATED_WORDS =
        Arrays.asList("начало", "конец");
    private InputText inputText;
    private TranslatedText translatedText;
    private Translation translation;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslationService translationService;

    @MockBean
    private TranslationRepository translationRepository;

    @BeforeEach
    void initTestData() {
        inputText =
            new InputText(INITIAL_TEXT, INITIAL_LANGUAGE, TARGET_LANGUAGE);
        translatedText = initTestTranslatedText();
        translation = initTestTranslation();
    }

    private TranslatedText initTestTranslatedText() {
        TranslatedText translatedText = new TranslatedText();
        translatedText.setInitialText(INITIAL_TEXT);
        translatedText.setInitialWords(INITIAL_WORDS);
        translatedText.setInitialLanguage(INITIAL_LANGUAGE);
        translatedText.setTargetLanguage(TARGET_LANGUAGE);
        translatedText.setResultText(TRANSLATED_TEXT);
        translatedText.setResultWords(TRANSLATED_WORDS);
        return translatedText;
    }

    private Translation initTestTranslation() {
        Translation translation = new Translation();
        translation.setId(TRANSLATION_ID);
        translation.setInitialText(INITIAL_TEXT);
        translation.setInitialWords(INITIAL_WORDS);
        translation.setInitialLanguage(INITIAL_LANGUAGE);
        translation.setTargetLanguage(TARGET_LANGUAGE);
        translation.setTranslatedText(TRANSLATED_TEXT);
        translation.setTranslatedWords(TRANSLATED_WORDS);
        return translation;
    }

    @Test
    public void translate_whenValidInputText_returnTranslation()
        throws Exception {
        final String translationUrl = "/translation";
        final String requestBody = "{\n" +
                                       "    \"initialLanguage\": \"en\",\n" +
                                       "    \"targetLanguage\": \"ru\",\n" +
                                       "    \"text\": \"start end\"\n" +
                                       "}";
        final int expectedId = 1;

        when(translationService.translate(inputText)).thenReturn(
            translatedText);
        when(translationRepository.add(any())).thenReturn(
            translation);

        mockMvc.perform(post(translationUrl).content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", Matchers.is(expectedId)))
            .andExpect(jsonPath("$.initialText",
                Matchers.is(translation.getInitialText())))
            .andExpect(jsonPath("$.initialWords",
                Matchers.is(translation.getInitialWords())))
            .andExpect(
                jsonPath("$.initialLanguage",
                    Matchers.is(translation.getInitialLanguage())))
            .andExpect(
                jsonPath("$.targetLanguage",
                    Matchers.is(translation.getTargetLanguage())))
            .andExpect(
                jsonPath("$.translatedText",
                    Matchers.is(translation.getTranslatedText())))
            .andExpect(
                jsonPath("$.translatedWords",
                    Matchers.is(translation.getTranslatedWords())));
    }
}
