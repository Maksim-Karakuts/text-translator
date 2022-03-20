package com.tinkoff.maksim.karakuts.text.translator;

import com.tinkoff.maksim.karakuts.text.translator.dto.InputText;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslatedText;
import com.tinkoff.maksim.karakuts.text.translator.dto.Translation;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslationData;
import com.tinkoff.maksim.karakuts.text.translator.repository.TranslationRepository;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/translation")
public class TranslationController {
    private static final Logger LOGGER =
        LoggerFactory.getLogger(TranslationController.class);
    private final TranslationService translationService;
    private final TranslationRepository translationRepository;

    @Autowired
    public TranslationController(TranslationService translationService,
                                 TranslationRepository translationRepository) {
        this.translationService = translationService;
        this.translationRepository = translationRepository;
    }

    @PostMapping
    public Translation translate(@RequestBody InputText text,
                                 HttpServletRequest request) {
        LOGGER.debug("User requests translation for {}", text);
        LocalDateTime requestDateTime = LocalDateTime.now();
        String clientIpAddress = request.getRemoteAddr();
        TranslatedText translatedText = translationService.translate(text);

        TranslationData
            translationData =
            initTranslationData(text,
                translatedText, requestDateTime, clientIpAddress);
        Translation translation = translationRepository.add(translationData);

        return translation;
    }

    private TranslationData initTranslationData(InputText inputText,
                                                TranslatedText translatedText,
                                                LocalDateTime requestDateTime,
                                                String clientIpAddress) {
        TranslationData translationData = new TranslationData();
        translationData.setInitialText(inputText.getText());
        translationData.setInitialWords(translatedText.getInitialWords());
        translationData.setInitialLanguage(inputText.getInitialLanguage());
        translationData.setTargetLanguage(inputText.getTargetLanguage());
        translationData.setTranslatedText(translatedText.getResultText());
        translationData.setTranslatedWords(translatedText.getResultWords());
        translationData.setRequestTime(requestDateTime);
        translationData.setClientIp(clientIpAddress);
        return translationData;
    }
}
