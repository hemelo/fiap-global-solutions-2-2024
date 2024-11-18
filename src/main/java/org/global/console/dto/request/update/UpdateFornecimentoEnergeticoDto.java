package org.global.console.dto.request.update;

import jakarta.validation.constraints.NotNull;

public record UpdateFornecimentoEnergeticoDto(

        @NotNull
        Long id,

        @NotNull(message = "Comunidade ID é obrigatório")
        Long comunidadeId,

        @NotNull(message = "Polo ID é obrigatório")
        Long poloId,

        @NotNull(message = "População é obrigatório")
        Long populacao
) {}