package com.tinkoff.maksim.karakuts.text.translator;

import com.tinkoff.maksim.karakuts.text.translator.dto.InputText;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslatedText;
import com.tinkoff.maksim.karakuts.text.translator.dto.Translation;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslationData;
import com.tinkoff.maksim.karakuts.text.translator.repository.TranslationRepository;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/translation")
public class TranslationController {
    private final TranslationService translationService;
    private final TranslationRepository translationRepository;

    @Autowired
    public TranslationController(TranslationService translationService,
                                 TranslationRepository translationRepository) {
        this.translationService = translationService;
        this.translationRepository = translationRepository;
    }

    @PostMapping
    public TranslatedText translate(@RequestBody InputText text,
                                    HttpServletRequest request) {
        LocalDateTime requestDateTime = LocalDateTime.now();
        TranslatedText translatedText = translationService.translate(text);
        TranslationData translationData = new TranslationData();
        translationData.setInputText(text);
        translationData.setTranslatedText(translatedText);
        translationData.setRequestTime(requestDateTime);
        translationData.setClientIp(request.getRemoteAddr());

        Translation translation = translationRepository.add(translationData);

        return translation.getTranslatedText();
    }
}
