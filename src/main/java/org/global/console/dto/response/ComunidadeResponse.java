package org.global.console.dto.response;

import java.time.LocalDateTime;

public record ComunidadeResponse(
        Long id,
        String nome,
        String localizacao,
        Double latitude,
        Double longitude,
        Long populacao,
        String descricao,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}