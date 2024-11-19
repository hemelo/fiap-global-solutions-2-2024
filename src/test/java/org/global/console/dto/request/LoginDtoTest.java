package org.global.console.dto.request;

import jakarta.validation.ConstraintViolation;
import org.global.console.dto.request.update.UpdateUserDto;
import org.global.console.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginDtoTest {

    @Test
    void testValidLoginDto() {
        LoginDto dto = new LoginDto("username", "password");
        Set<ConstraintViolation<LoginDto>> violations = ValidationUtils.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidLoginDto() {
        LoginDto dto = new LoginDto(null, null);
        Set<ConstraintViolation<LoginDto>> violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new LoginDto("username", "");
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new LoginDto("", "password");
        violations = ValidationUtils.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
