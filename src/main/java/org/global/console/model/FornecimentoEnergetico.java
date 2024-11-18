package org.global.console.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class FornecimentoEnergetico {
    private Long id;
    private Long comunidadeId;
    private Long poloId;
    private Long populacao;

    private Comunidade comunidade;
    private PoloFornecedor poloFornecedor;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}