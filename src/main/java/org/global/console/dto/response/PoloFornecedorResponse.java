package org.global.console.dto.response;

import java.time.LocalDateTime;

public record PoloFornecedorResponse (
    Long id,
    String nome,
    Double latitude,
    Double longitude,
    String endereco,
    Long idFornecedor,
    Long idEnergia,
    Long capacidadePopulacao,
    Long capacidadePopulacaoMaxima,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}