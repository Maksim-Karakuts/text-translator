package com.tinkoff.maksim.karakuts.text.translator.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TranslationData {
    private InputText inputText;
    private TranslatedText translatedText;
    private LocalDateTime requestTime;
    private String clientIp;
}
