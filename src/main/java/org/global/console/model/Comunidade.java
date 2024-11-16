package org.global.console.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comunidade {
    private Long id;
    private String nome;
    private String localizacao;
    private Double latitude;
    private Double longitude;
    private String descricao;
    private Long populacao;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}