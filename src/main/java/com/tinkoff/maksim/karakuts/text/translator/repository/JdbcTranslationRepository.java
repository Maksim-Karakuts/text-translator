package com.tinkoff.maksim.karakuts.text.translator.repository;

import com.tinkoff.maksim.karakuts.text.translator.dto.InputText;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslatedText;
import com.tinkoff.maksim.karakuts.text.translator.dto.Translation;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslationData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTranslationRepository implements TranslationRepository {
    private static final int INITIAL_WORD_COLUMN_INDEX = 2;
    private static final Logger LOGGER =
        LoggerFactory.getLogger(JdbcTranslationRepository.class);
    private static final int TRANSLATED_WORD_COLUMN_INDEX = 3;
    private static final int TRANSLATION_ID_COLUMN_INDEX = 1;
    private static final String TRANSLATION_TABLE_NAME = "TRANSLATION";
    private static final String ID_COLUMN_NAME = "id";
    private static final String INITIAL_TEXT_COLUMN_NAME = "initial_text";
    private static final String TRANSLATED_TEXT_COLUMN_NAME = "translated_text";
    private static final String FROM_LANGUAGE_COLUMN_NAME = "from_language";
    private static final String TO_LANGUAGE_COLUMN_NAME = "to_language";
    private static final String REQUEST_TIME_COLUMN_NAME = "request_time";
    private static final String USER_IP_COLUMN_NAME = "user_ip";
    private static final String WORD_DELIMITER = " ";
    private static final String INSERT_INTO_WORD_QUERY =
        "INSERT INTO WORD (translation_id, initial_word, " +
            "translated_word) values(?,?,?)";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public JdbcTranslationRepository(DataSource dataSource,
                                     JdbcTemplate jdbcTemplate) {
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName(TRANSLATION_TABLE_NAME).usingGeneratedKeyColumns(
                ID_COLUMN_NAME);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Translation add(TranslationData translationData) {
        Translation translation = addTranslation(translationData);
        addWords(translation);
        return translation;
    }

    private Translation addTranslation(TranslationData translationData) {
        LOGGER.debug("Adding {} to {} in database", translationData,
            TRANSLATION_TABLE_NAME);
        InputText inputText = translationData.getInputText();
        TranslatedText translatedText = translationData.getTranslatedText();

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(INITIAL_TEXT_COLUMN_NAME, inputText.getText());
        queryParameters.put(TRANSLATED_TEXT_COLUMN_NAME,
            translatedText.getText());
        queryParameters.put(FROM_LANGUAGE_COLUMN_NAME,
            inputText.getInitialLanguage());
        queryParameters.put(TO_LANGUAGE_COLUMN_NAME,
            inputText.getTargetLanguage());
        queryParameters.put(REQUEST_TIME_COLUMN_NAME,
            Timestamp.valueOf(translationData.getRequestTime()));
        queryParameters.put(USER_IP_COLUMN_NAME, translationData.getClientIp());

        long translationId =
            simpleJdbcInsert.executeAndReturnKey(queryParameters).longValue();
        Translation translation =
            new Translation(translationId, inputText,
                translatedText,
                translationData.getRequestTime(),
                translationData.getClientIp());

        LOGGER.debug("Added {} to {} in database", translation,
            TRANSLATION_TABLE_NAME);
        return translation;

    }

    private void addWords(Translation translation) {
        LOGGER.trace("Enter adding words with {}", translation);
        String[] initialWords =
            translation.getInputText().getText().split(WORD_DELIMITER);
        String[] translatedWords =
            translation.getTranslatedText().getText().split(WORD_DELIMITER);

        this.jdbcTemplate.batchUpdate(INSERT_INTO_WORD_QUERY,
            new BatchPreparedStatementSetter() {

                public void setValues(PreparedStatement statement, int i)
                    throws SQLException {
                    statement.setInt(TRANSLATION_ID_COLUMN_INDEX,
                        (int) translation.getId());
                    statement.setString(INITIAL_WORD_COLUMN_INDEX,
                        initialWords[i]);
                    statement.setString(TRANSLATED_WORD_COLUMN_INDEX,
                        translatedWords[i]);
                }

                public int getBatchSize() {
                    return initialWords.length;
                }
            });
        LOGGER.trace("Exit adding words");
    }
}
