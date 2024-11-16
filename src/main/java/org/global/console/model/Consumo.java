package org.global.console.model;

import lombok.*;
import org.global.console.enums.UnidadeMedida;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class Consumo {
    private Long id;
    private Long comunidadeId;
    private Long energiaId;
    private Long fornecedorId;
    private Double quantidade;
    private UnidadeMedida unidade;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}