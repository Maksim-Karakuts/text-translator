package com.tinkoff.maksim.karakuts.text.translator.dto;

import java.util.List;
import lombok.Data;

@Data
public class TranslatedText {
    private String initialText;
    private List<String> initialWords;
    private String initialLanguage;
    private String targetLanguage;
    private String resultText;
    private List<String> resultWords;
}

