package org.global.console.dto.request.create;

import jakarta.validation.ConstraintViolation;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreatePoloFornecedorDtoTest {

    @Test
    public void testValidCreatePoloFornecedorDto() {
        CreatePoloFornecedorDto dto = new CreatePoloFornecedorDto("Nome", "Endereço", 10.0, 20.0, 25L, 1L, 20L, 40L);
        Set<ConstraintViolation<CreatePoloFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidCreatePoloFornecedorDto() {
        CreatePoloFornecedorDto dto = new CreatePoloFornecedorDto(null, "Endereço", 10.0, 20.0, 25L, 1L, 20L, 40L);
        Set<ConstraintViolation<CreatePoloFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreatePoloFornecedorDto("Nome", "", 10.0, 20.0, 25L, 1L, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreatePoloFornecedorDto("Nome", "Endereço", null, 20.0, 25L, 1L, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreatePoloFornecedorDto("Nome", "Endereço", 10.0, null, 25L, 1L, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreatePoloFornecedorDto("Nome", "Endereço", 10.0, 20.0, null, 1L, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreatePoloFornecedorDto(null, null, null, null, null, 1L, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreatePoloFornecedorDto("Nome", "Endereço", 10.0, 20.0, 25L, null, 20L, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreatePoloFornecedorDto("Nome", "Endereço", 10.0, 20.0, 25L, 1L, null, 40L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new CreatePoloFornecedorDto("Nome", "Endereço", 10.0, 20.0, 25L, 1L, 20L, null);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
