package com.tinkoff.maksim.karakuts.text.translator.validation;

import com.tinkoff.maksim.karakuts.text.translator.dto.InputText;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputTextValidationTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    void validate_whenValidInputText_thenNoConstraintViolations() {
        final InputText inputText = new InputText();
        inputText.setText("Valid text");
        inputText.setInitialLanguage("en");
        inputText.setTargetLanguage("ru");

        Set<ConstraintViolation<InputText>> violations =
            validator.validate(inputText);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validate_whenInvalidInitialLanguage_thenInitialLanguageConstraintViolated() {
        final InputText inputText = new InputText();
        inputText.setText("Valid text");
        inputText.setInitialLanguage("invalid language");
        inputText.setTargetLanguage("es");
        final int expectedViolationsCount = 1;
        final String expectedViolatedFieldName = "initialLanguage";
        final String expectedViolationMessage = "Language is not supported";

        Set<ConstraintViolation<InputText>> violations =
            validator.validate(inputText);

        assertThat(violations.size()).isEqualTo(expectedViolationsCount);
        assertThat(violations).anyMatch(
            violation -> violation.getPropertyPath().toString()
                .equals(expectedViolatedFieldName));
        assertThat(violations).anyMatch(violation -> violation.getMessage()
            .equals(expectedViolationMessage));
    }

    @Test
    void validate_whenTargetLanguageIsNull_thenTargetLanguageConstraintViolated() {
        final InputText inputText = new InputText();
        inputText.setText("Valid text");
        inputText.setInitialLanguage("it");
        inputText.setTargetLanguage(null);
        final int expectedViolationsCount = 1;
        final String expectedViolatedFieldName = "targetLanguage";
        final String expectedViolationMessage = "Language is not supported";

        Set<ConstraintViolation<InputText>> violations =
            validator.validate(inputText);

        assertThat(violations.size()).isEqualTo(expectedViolationsCount);
        assertThat(violations).anyMatch(
            violation -> violation.getPropertyPath().toString()
                .equals(expectedViolatedFieldName));
        assertThat(violations).anyMatch(violation -> violation.getMessage()
            .equals(expectedViolationMessage));
    }

    @Test
    void validate_whenEmptyText_thenTextConstraintViolated() {
        final InputText inputText = new InputText();
        inputText.setText("");
        inputText.setInitialLanguage("fr");
        inputText.setTargetLanguage("it");
        final int expectedViolationsCount = 1;
        final String expectedViolatedFieldName = "text";
        final String expectedViolationMessage = "Text to translate is required";

        Set<ConstraintViolation<InputText>> violations =
            validator.validate(inputText);

        assertThat(violations.size()).isEqualTo(expectedViolationsCount);
        assertThat(violations).anyMatch(
            violation -> violation.getPropertyPath().toString()
                .equals(expectedViolatedFieldName));
        assertThat(violations).anyMatch(violation -> violation.getMessage()
            .equals(expectedViolationMessage));
    }
}
