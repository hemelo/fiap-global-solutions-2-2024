package org.global.console.dto.request.update;

import jakarta.validation.ConstraintViolation;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateEnergiaDtoTest {


    @Test
    void testValidUpdateEnergiaDto() {
        UpdateEnergiaDto dto = new UpdateEnergiaDto(1L, "Valid Name", "Valid Description", null, "Valid Type");
        Set<ConstraintViolation<UpdateEnergiaDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidUpdateEnergiaDto() {
        UpdateEnergiaDto dto = new UpdateEnergiaDto(null, null, null, null, null);
        Set<ConstraintViolation<UpdateEnergiaDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}