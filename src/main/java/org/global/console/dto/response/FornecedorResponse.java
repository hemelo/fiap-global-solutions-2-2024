package org.global.console.dto.response;


import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record FornecedorResponse (
    Long id,
    String nome,
    String cnpj,
    String endereco,
    List<PoloFornecedorResponse> polos,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}