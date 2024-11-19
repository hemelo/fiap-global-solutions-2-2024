package org.global.console.dto.request.create;

import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateEnergiaDtoTest {

    @Test
    void testValidCreateEnergiaDto() {
        CreateEnergiaDto dto = new CreateEnergiaDto("Valid Name", "Valid Description", null, "Valid Type");
        Set<ConstraintViolation<CreateEnergiaDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidCreateEnergiaDto() {
        CreateEnergiaDto dto = new CreateEnergiaDto("", "", null, "");
        Set<ConstraintViolation<CreateEnergiaDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}