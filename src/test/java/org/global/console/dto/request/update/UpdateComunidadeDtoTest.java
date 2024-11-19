package org.global.console.dto.request.update;

import jakarta.validation.ConstraintViolation;
import org.global.console.dto.request.update.UpdateComunidadeDto;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateComunidadeDtoTest {

    @Test
    void testValidUpdateComunidadeDto() {
        UpdateComunidadeDto dto = new UpdateComunidadeDto(1L, "Comunidade 1", "Descrição da Comunidade 1", "comunidade1", 10.0, 20.0, 25000L);
        Set<ConstraintViolation<UpdateComunidadeDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidUpdateComunidadeDto() {
        UpdateComunidadeDto dto = new UpdateComunidadeDto(2L, "Comunidade 2", "Descrição da Comunidade 2", "comunidade2", 10.0, 20.0, -25000L);
        Set<ConstraintViolation<UpdateComunidadeDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateComunidadeDto(3L, "Comunidade 3", "Descrição da Comunidade 3", "comunidade3", 95.0, 190.0, 25000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateComunidadeDto(4L, "", "Descrição da Comunidade 4", "comunidade4", 10.0, 20.0, 2000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateComunidadeDto(5L, "Comunidade 5", "", "comunidade5", 10.0, 20.0, 2000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateComunidadeDto(6L, "Comunidade 6", "Descrição da Comunidade 6", "", 10.0, 20.0, 2000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateComunidadeDto(7L, "Comunidade 7", "Descrição da Comunidade 7", "comunidade7", null, 20.0, 2000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateComunidadeDto(8L, "Comunidade 8", "Descrição da Comunidade 8", "comunidade8", 10.0, null, 2000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateComunidadeDto(9L, "Comunidade 9", "Descrição da Comunidade 9", "comunidade9", 10.0, 20.0, null);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdateComunidadeDto(null, "Comunidade 10", "Descrição da Comunidade 10", "comunidade10", 10.0, 20.0, 2000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());


    }
}