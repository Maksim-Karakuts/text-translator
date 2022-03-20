package com.tinkoff.maksim.karakuts.text.translator.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Translation {
    private long id;
    private String initialText;
    private List<String> initialWords;
    private String initialLanguage;
    private String targetLanguage;
    private String translatedText;
    private List<String> translatedWords;
}
