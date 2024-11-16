package org.global.console.dto.request.update;

public record UpdateConsumoDto(
        Long id,
        Long comunidadeId,
        Long energiaId
) {}