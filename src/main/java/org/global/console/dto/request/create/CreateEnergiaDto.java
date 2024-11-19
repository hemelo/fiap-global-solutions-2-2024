package org.global.console.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import org.global.console.enums.UnidadeMedida;

public record CreateEnergiaDto(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        //@NotNull(message = "Unidade de media é obrigatória")
        UnidadeMedida unidade,

        @NotBlank(message = "Tipo é obrigatório")
        String tipo
) {}
