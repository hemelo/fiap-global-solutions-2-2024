package org.global.console.dto.request.update;

import jakarta.validation.ConstraintViolation;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateUserDtoTest {

    @Test
    void testValidUpdateUserDto() {
        UpdateUserDto dto = new UpdateUserDto("login", "nome", "email@gmail.com", "senha");
        Set<ConstraintViolation<UpdateUserDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmailUpdateUserDto() {
        UpdateUserDto dto = new UpdateUserDto("login", "nome", "email", "senha");
        Set<ConstraintViolation<UpdateUserDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidUpdateUserDto() {
        UpdateUserDto dto = new UpdateUserDto("", "", "", "");
        Set<ConstraintViolation<UpdateUserDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}