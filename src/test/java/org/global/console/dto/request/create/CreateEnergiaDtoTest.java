package org.global.console.dto.request.create;

import jakarta.validation.ConstraintViolation;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

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