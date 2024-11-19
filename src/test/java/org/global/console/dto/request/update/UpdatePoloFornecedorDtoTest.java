package org.global.console.dto.request.update;

import jakarta.validation.ConstraintViolation;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdatePoloFornecedorDtoTest {

    @Test
    void testValidUpdatePoloFornecedorDto() {
        UpdatePoloFornecedorDto dto = new UpdatePoloFornecedorDto(1L, "Nome", "Endereço", 20.0, 30.0, 1L, 1L, 20L, 40L);
        Set<ConstraintViolation<UpdatePoloFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidUpdatePoloFornecedorDto() {
        UpdatePoloFornecedorDto dto = new UpdatePoloFornecedorDto(1L, null, "Endereço", 20.0, 30.0, 1L, 1L, 20L, 40L);
        Set<ConstraintViolation<UpdatePoloFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, "Nome", "", 20.0, 30.0, 1L, 1L, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, "Nome", "Endereço", null, 30.0, 1L, 1L, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, "Nome", "Endereço", 20.0, null, 1L, 1L, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, "Nome", "Endereço", 20.0, 30.0, null, 1L, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, null, null, null, null, null, 1L, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, "Nome", "Endereço", 20.0, 30.0, 1L, null, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, "Nome", "Endereço", 20.0, 30.0, 1L, 1L, null, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, "Nome", "Endereço", 20.0, 30.0, 1L, 1L, 20L, null);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

    }
}
