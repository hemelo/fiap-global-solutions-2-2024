package org.global.console.dto.request.create;

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

class CreateUserDtoTest {

    @Test
    void testValidCreateUserDto() {
        CreateUserDto dto = new CreateUserDto("login", "name", "email@gmail.com", "password");
        Set<ConstraintViolation<CreateUserDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmailCreateUserDto() {
        CreateUserDto dto = new CreateUserDto("login", "name", "email", "password");
        Set<ConstraintViolation<CreateUserDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidCreateUserDto() {
        CreateUserDto dto = new CreateUserDto("", "", "", "");
        Set<ConstraintViolation<CreateUserDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}