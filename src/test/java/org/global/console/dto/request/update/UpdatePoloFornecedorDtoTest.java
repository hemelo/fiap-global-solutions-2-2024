package org.global.console.dto.request.update;

import jakarta.validation.ConstraintViolation;
import org.global.console.dto.request.create.CreatePoloFornecedorDto;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdatePoloFornecedorDtoTest {

    @Test
    public void testValidUpdatePoloFornecedorDto() {
        UpdatePoloFornecedorDto dto = new UpdatePoloFornecedorDto(1L, "Nome", "Endereço", 20.0, 30.0, 1L);
        Set<ConstraintViolation<UpdatePoloFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidUpdatePoloFornecedorDto() {
        UpdatePoloFornecedorDto dto = new UpdatePoloFornecedorDto(1L, null, "Endereço", 20.0, 30.0, 1L);
        Set<ConstraintViolation<UpdatePoloFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, "Nome", "", 20.0, 30.0, 1L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, "Nome", "Endereço", null, 30.0, 1L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, "Nome", "Endereço", 20.0, null, 1L);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, "Nome", "Endereço", 20.0, 30.0, null);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new UpdatePoloFornecedorDto(1L, null, null, null, null, null);
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
