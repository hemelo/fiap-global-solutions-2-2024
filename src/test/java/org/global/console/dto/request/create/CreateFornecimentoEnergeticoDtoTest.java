package org.global.console.dto.request.create;

import jakarta.validation.ConstraintViolation;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateFornecimentoEnergeticoDtoTest {

    @Test
    public void testValidCreateFornecimentoEnergeticoDto() {
        CreateFornecimentoEnergeticoDto dto = new CreateFornecimentoEnergeticoDto(1L, 1L, 20L);
        Set<ConstraintViolation<CreateFornecimentoEnergeticoDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidCreateFornecimentoEnergeticoDto() {
        CreateFornecimentoEnergeticoDto dto = new CreateFornecimentoEnergeticoDto(null, null, null);
        Set<ConstraintViolation<CreateFornecimentoEnergeticoDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreateFornecimentoEnergeticoDto(1L, null, 20L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreateFornecimentoEnergeticoDto(1L, 1L, null);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreateFornecimentoEnergeticoDto(null, 1L, 20L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
