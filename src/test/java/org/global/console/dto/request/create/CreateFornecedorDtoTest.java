package org.global.console.dto.request.create;

import jakarta.validation.ConstraintViolation;
import org.global.console.dto.request.update.UpdateFornecedorDto;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateFornecedorDtoTest {

    @Test
    public void testValidCreateFornecedorDto() {
        CreateFornecedorDto dto = new CreateFornecedorDto("Nome", "CNPJ", "Endereco");
        Set<ConstraintViolation<CreateFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidCreateFornecedorDto() {
        CreateFornecedorDto dto = new CreateFornecedorDto(null, null, null);
        Set<ConstraintViolation<CreateFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
