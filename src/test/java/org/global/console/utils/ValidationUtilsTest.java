package org.global.console.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ValidationUtilsTest {

    @Test
    void testValidaEmail() {

        ObjectWithEmailValidation object = new ObjectWithEmailValidation();
        object.email = "qualquer_coisa";

        Set<ConstraintViolation<ObjectWithEmailValidation>> violations = ValidationUtils.validate(object);
        Assertions.assertEquals(1, violations.size());
    }

    private class ObjectWithEmailValidation {

        @Email
        private String email;
    }
}
