package org.global.console.dto.request.create;

import jakarta.validation.ConstraintViolation;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateFornecedorDtoTest {

    @Test
    void testValidCreateFornecedorDto() {
        CreateFornecedorDto dto = new CreateFornecedorDto("Nome", "CNPJ", "Endereco", "Descricao");
        Set<ConstraintViolation<CreateFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidCreateFornecedorDto() {
        CreateFornecedorDto dto = new CreateFornecedorDto(null, null, null, null);
        Set<ConstraintViolation<CreateFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
