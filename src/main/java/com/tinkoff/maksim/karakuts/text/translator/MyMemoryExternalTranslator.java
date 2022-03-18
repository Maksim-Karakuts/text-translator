package com.tinkoff.maksim.karakuts.text.translator;

import com.tinkoff.maksim.karakuts.text.translator.dto.mymemory.TranslationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class MyMemoryExternalTranslator implements ExternalTranslator {
    private static final String MY_MEMORY_API_URL =
        "https://api.mymemory.translated" +
            ".net/get?q=%s&langpair=%s|%s&de=vasilian3000@yandex.ru";
    private final RestTemplate restTemplate;

    @Autowired
    public MyMemoryExternalTranslator(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    @Override
    public CompletableFuture<String> translateWord(String word,
                                                   String initialLanguage,
                                                   String targetLanguage) {
        String apiUrl = String.format(
            MY_MEMORY_API_URL, word, initialLanguage, targetLanguage);
        Optional<TranslationResponse> response = Optional.ofNullable(
            restTemplate.getForObject(apiUrl, TranslationResponse.class));
        return CompletableFuture.completedFuture(
            response.orElseThrow(() -> new ExternalApiException(
                    "MyMemory API response is empty")).getResponseData()
                .getTranslatedText());
    }
}
