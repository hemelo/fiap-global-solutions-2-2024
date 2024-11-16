package org.global.console.dto.request.create;

import org.apache.commons.lang3.StringUtils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record CreateContatoDto (
        @Email(message = "Email com formato inválido")
        String email,

        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Telefone com formato inválido")
        String telefone
) {

    public CreateContatoDto {
        if (StringUtils.isNotBlank(email)) {
            email = email.trim();
        }

        if (StringUtils.isNotBlank(telefone)) {
            telefone = telefone.trim();
        }
    }
}