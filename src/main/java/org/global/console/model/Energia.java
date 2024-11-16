package org.global.console.model;

import lombok.*;
import org.global.console.enums.UnidadeMedida;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class Energia {
    private Long id;
    private String tipo;
    //private UnidadeMedida unidade;
    private String nome;
    private String descricao;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}