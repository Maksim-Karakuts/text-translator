package com.tinkoff.maksim.karakuts.text.translator;

import com.tinkoff.maksim.karakuts.text.translator.dto.InputText;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslatedText;

public interface TranslationService {
    TranslatedText translate(InputText text);
}
