package com.tinkoff.maksim.karakuts.text.translator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputText {
    private String text;
    private String initialLanguage;
    private String targetLanguage;
}
