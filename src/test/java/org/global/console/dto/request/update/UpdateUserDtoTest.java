package org.global.console.dto.request.update;

import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateUserDtoTest {

    @Test
    public void testValidUpdateUserDto() {
        UpdateUserDto dto = new UpdateUserDto("login", "nome", "email@gmail.com", "senha");
        Set<ConstraintViolation<UpdateUserDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidEmailUpdateUserDto() {
        UpdateUserDto dto = new UpdateUserDto("login", "nome", "email", "senha");
        Set<ConstraintViolation<UpdateUserDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testInvalidUpdateUserDto() {
        UpdateUserDto dto = new UpdateUserDto("", "", "", "");
        Set<ConstraintViolation<UpdateUserDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}