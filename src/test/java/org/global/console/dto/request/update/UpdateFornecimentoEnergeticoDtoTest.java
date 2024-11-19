package org.global.console.dto.request.update;

import jakarta.validation.ConstraintViolation;
import org.global.console.dto.request.update.UpdateFornecimentoEnergeticoDto;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateFornecimentoEnergeticoDtoTest {

    @Test
    void testValidUpdateFornecedorDto() {
        UpdateFornecimentoEnergeticoDto dto = new UpdateFornecimentoEnergeticoDto(1L, 1L, 1L, 20L);
        Set<ConstraintViolation<UpdateFornecimentoEnergeticoDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidUpdateFornecedorDto() {
        UpdateFornecimentoEnergeticoDto dto = new UpdateFornecimentoEnergeticoDto(null, null, null, null);
        Set<ConstraintViolation<UpdateFornecimentoEnergeticoDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateFornecimentoEnergeticoDto(1L, 1L, null, 20L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateFornecimentoEnergeticoDto(1L, 1L, 1L, null);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateFornecimentoEnergeticoDto(1L, null, 1L, 20L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateFornecimentoEnergeticoDto(null, 1L, 1L, 20L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
