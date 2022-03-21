package com.tinkoff.maksim.karakuts.text.translator.validation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator
    implements ConstraintValidator<EnumValue, CharSequence> {
    private List<String> acceptedValues;

    @Override
    public void initialize(EnumValue annotation) {
        acceptedValues =
            Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value,
                           ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return acceptedValues.contains(value.toString().toUpperCase());
    }
}