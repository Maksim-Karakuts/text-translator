package com.tinkoff.maksim.karakuts.text.translator.dto;

import com.tinkoff.maksim.karakuts.text.translator.validation.EnumValue;
import com.tinkoff.maksim.karakuts.text.translator.validation.Language;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputText {
    @NotBlank(message = "Text to translate is required")
    private String text;

    @EnumValue(enumClass = Language.class,
        message = "Language is not supported")
    private String initialLanguage;

    @EnumValue(enumClass = Language.class,
        message = "Language is not supported")
    private String targetLanguage;
}
