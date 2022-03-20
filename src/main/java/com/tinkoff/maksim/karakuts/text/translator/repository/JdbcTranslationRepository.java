package com.tinkoff.maksim.karakuts.text.translator.repository;

import com.tinkoff.maksim.karakuts.text.translator.dto.Translation;
import com.tinkoff.maksim.karakuts.text.translator.dto.TranslationData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
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

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(INITIAL_TEXT_COLUMN_NAME,
            translationData.getInitialText());
        queryParameters.put(TRANSLATED_TEXT_COLUMN_NAME,
            translationData.getTranslatedText());
        queryParameters.put(FROM_LANGUAGE_COLUMN_NAME,
            translationData.getInitialLanguage());
        queryParameters.put(TO_LANGUAGE_COLUMN_NAME,
            translationData.getTargetLanguage());
        queryParameters.put(REQUEST_TIME_COLUMN_NAME,
            Timestamp.valueOf(translationData.getRequestTime()));
        queryParameters.put(USER_IP_COLUMN_NAME, translationData.getClientIp());

        long translationId =
            simpleJdbcInsert.executeAndReturnKey(queryParameters).longValue();
        Translation translation =
            initTranslation(translationData, translationId);

        LOGGER.debug("Added {} to {} in database", translation,
            TRANSLATION_TABLE_NAME);
        return translation;
    }

    private Translation initTranslation(TranslationData translationData,
                                        long translationId) {
        Translation translation = new Translation();
        translation.setId(translationId);
        translation.setInitialText(translationData.getInitialText());
        translation.setInitialWords(translationData.getInitialWords());
        translation.setInitialLanguage(translationData.getInitialLanguage());
        translation.setTargetLanguage(translationData.getTargetLanguage());
        translation.setTranslatedText(translationData.getTranslatedText());
        translation.setTranslatedWords(translationData.getTranslatedWords());
        return translation;
    }

    private void addWords(Translation translation) {
        LOGGER.trace("Enter adding words with {}", translation);
        List<String> initialWords = translation.getInitialWords();
        List<String> translatedWords = translation.getTranslatedWords();

        this.jdbcTemplate.batchUpdate(INSERT_INTO_WORD_QUERY,
            new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement statement, int i)
                    throws SQLException {
                    statement.setInt(TRANSLATION_ID_COLUMN_INDEX,
                        (int) translation.getId());
                    statement.setString(INITIAL_WORD_COLUMN_INDEX,
                        initialWords.get(i));
                    statement.setString(TRANSLATED_WORD_COLUMN_INDEX,
                        translatedWords.get(i));
                }

                public int getBatchSize() {
                    return initialWords.size();
                }
            });
        LOGGER.trace("Exit adding words");
    }
}
