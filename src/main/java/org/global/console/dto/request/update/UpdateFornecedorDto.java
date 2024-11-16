package org.global.console.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateFornecedorDto (

        @NotNull(message = "ID é obrigatório")
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        String nome,
        String cnpj,
        String endereco
) {}