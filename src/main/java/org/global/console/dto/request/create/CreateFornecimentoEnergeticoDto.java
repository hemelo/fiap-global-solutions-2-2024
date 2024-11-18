package org.global.console.dto.request.create;

import jakarta.validation.constraints.NotNull;

public record CreateFornecimentoEnergeticoDto(

        @NotNull(message = "Comunidade ID é obrigatório")
        Long comunidadeId,

        @NotNull(message = "Polo ID é obrigatório")
        Long poloId,

        @NotNull(message = "População é obrigatório")
        Long populacao
) {}