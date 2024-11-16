package org.global.console.dto.response;

import java.time.LocalDateTime;

public record PoloFornecedorResponse (
    Long id,
    String nome,
    Double latitude,
    Double longitude,
    String endereco,
    Long idFornecedor,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}