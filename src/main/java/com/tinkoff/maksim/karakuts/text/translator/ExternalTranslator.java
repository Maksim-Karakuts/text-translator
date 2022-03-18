package com.tinkoff.maksim.karakuts.text.translator;

import java.util.concurrent.CompletableFuture;

public interface ExternalTranslator {
    CompletableFuture<String> translateWord(String word,
                                            String initialLanguage,
                                            String targetLanguage);
}
