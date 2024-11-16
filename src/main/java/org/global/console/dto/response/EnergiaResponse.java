package org.global.console.dto.response;

import org.global.console.enums.UnidadeMedida;

import java.time.LocalDateTime;

public record EnergiaResponse (
        Long id,
        String tipo,
        String nome,
        String descricao,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        //String unidade,
) {

}
