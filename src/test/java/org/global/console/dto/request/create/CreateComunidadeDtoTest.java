package org.global.console.dto.request.create;

import jakarta.validation.ConstraintViolation;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateComunidadeDtoTest {

    @Test
    void testValidCreateComunidadeDto() {
        CreateComunidadeDto dto = new CreateComunidadeDto("Comunidade 1", "Descrição da Comunidade 1", "comunidade1", 10.0, 20.0, 25000L);
        Set<ConstraintViolation<CreateComunidadeDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidCreateComunidadeDto() {
        CreateComunidadeDto dto = new CreateComunidadeDto("Comunidade 2", "Descrição da Comunidade 2", "comunidade2", 10.0, 20.0, -25000L);
        Set<ConstraintViolation<CreateComunidadeDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreateComunidadeDto("Comunidade 3", "Descrição da Comunidade 3", "comunidade3", 95.0, 190.0, 25000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreateComunidadeDto("", "Descrição da Comunidade 4", "comunidade4", 10.0, 20.0, 2000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreateComunidadeDto("Comunidade 5", "", "comunidade5", 10.0, 20.0, 2000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreateComunidadeDto("Comunidade 6", "Descrição da Comunidade 6", "", 10.0, 20.0, 2000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreateComunidadeDto("Comunidade 7", "Descrição da Comunidade 7", "comunidade7", null, 20.0, 2000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreateComunidadeDto("Comunidade 8", "Descrição da Comunidade 8", "comunidade8", 10.0, null, 2000L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreateComunidadeDto("Comunidade 9", "Descrição da Comunidade 9", "comunidade9", 10.0, 20.0, null);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreateComunidadeDto("Comunidade 10", "Descrição da Comunidade 10", "comunidade10", null, null, null);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}