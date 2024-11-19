package org.global.console.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.HashSet;
import java.util.Set;

public class ValidationUtils {

    private static ValidatorFactory factory;
    private static Validator validator;

    public static <T> Set<ConstraintViolation<T>> validate(T object) {

        if (factory == null)
            factory = Validation.buildDefaultValidatorFactory();

        if (validator == null)
            validator = factory.getValidator();

        if (object == null)
            throw new IllegalArgumentException("Object cannot be null");

        if (validator == null)
            return new HashSet<>();

        return validator.validate(object);
    }
}
