package org.global.console.dto.response;


import java.time.LocalDateTime;
import java.util.List;

public record FornecedorResponse (
    Long id,
    String nome,
    String cnpj,
    String descricao,
    String endereco,
    List<PoloFornecedorResponse> polos,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}