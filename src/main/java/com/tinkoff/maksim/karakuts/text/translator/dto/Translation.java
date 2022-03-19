package com.tinkoff.maksim.karakuts.text.translator.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Translation {
    private long id;
    private InputText inputText;
    private TranslatedText translatedText;
    private LocalDateTime requestTime;
    private String clientIp;
}
