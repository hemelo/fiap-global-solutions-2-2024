package org.global.console.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FornecimentoEnergeticoResponse (
        Long id,
        Long comunidadeId,
        Long poloId,
        Long fornecedorId,
        Long energiaId,
        Long populacaoFornecida,
        ComunidadeResponse comunidade,
        PoloFornecedorResponse polo,
        FornecedorResponse fornecedor,
        EnergiaResponse energia,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
