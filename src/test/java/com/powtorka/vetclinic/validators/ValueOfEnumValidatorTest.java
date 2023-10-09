package com.powtorka.vetclinic.validators;

import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValueOfEnumValidatorTest {

    @InjectMocks
    private ValueOfEnumValidator validator;

    @Mock
    private ValueOfEnum valueOfEnum;

    @Mock
    private ConstraintValidatorContext context;

    public enum TestEnum {
        VALUE1, VALUE2, VALUE3;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ValueOfEnum valueOfEnum = new ValueOfEnum() {
            @Override
            public Class<? extends Enum<?>> enumClass() {
                return TestEnum.class;
            }

            @Override
            public String message() {
                return "must be any of enum {enumClass}";
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ValueOfEnum.class;
            }
        };

        validator.initialize(valueOfEnum);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenValueIsValidEnumName() {
        assertTrue(validator.isValid("VALUE1", context));
        assertTrue(validator.isValid("VALUE2", context));
        assertTrue(validator.isValid("VALUE3", context));
    }

    @Test
    void isValid_ShouldReturnFalse_WhenValueIsInvalidEnumName() {
        assertFalse(validator.isValid("INVALID", context));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenValueIsNull() {
        assertTrue(validator.isValid(null, context));
    }

}