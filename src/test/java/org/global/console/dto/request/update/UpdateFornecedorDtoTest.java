package org.global.console.dto.request.update;

import jakarta.validation.ConstraintViolation;
import org.global.console.dto.request.update.UpdateFornecedorDto;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateFornecedorDtoTest {

    @Test
    public void testValidUpdateFornecedorDto() {
        UpdateFornecedorDto dto = new UpdateFornecedorDto(1L, "Nome", "CNPJ", "Endereco", "Descricao");
        Set<ConstraintViolation<UpdateFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidUpdateFornecedorDto() {
        UpdateFornecedorDto dto = new UpdateFornecedorDto(2L, null, null, null, null);
        Set<ConstraintViolation<UpdateFornecedorDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}