package org.global.console.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginDto (
        @NotBlank(message = "O campo username é obrigatório")
        String username,

        @NotBlank(message = "O campo senha é obrigatório")
        String senha) {
}
