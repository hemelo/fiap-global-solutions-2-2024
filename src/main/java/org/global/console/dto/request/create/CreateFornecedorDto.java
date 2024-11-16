package org.global.console.dto.request.create;

import jakarta.validation.constraints.NotBlank;

public record CreateFornecedorDto (
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String cnpj,
        String endereco
) {}