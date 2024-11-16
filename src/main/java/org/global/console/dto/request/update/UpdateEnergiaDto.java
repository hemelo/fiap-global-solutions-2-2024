package org.global.console.dto.request.update;

import org.global.console.enums.UnidadeMedida;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateEnergiaDto (

        @NotNull(message = "ID é obrigatório")
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        //@NotNull(message = "Unidade é obrigatória")
        UnidadeMedida unidade,

        @NotBlank(message = "Tipo é obrigatório")
        String tipo
) {}