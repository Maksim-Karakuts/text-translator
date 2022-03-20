package com.tinkoff.maksim.karakuts.text.translator.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TranslationData {
    private String initialText;
    private List<String> initialWords;
    private String initialLanguage;
    private String targetLanguage;
    private String translatedText;
    private List<String> translatedWords;
    private LocalDateTime requestTime;
    private String clientIp;
}
