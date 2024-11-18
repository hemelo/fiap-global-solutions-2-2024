package org.global.console.model;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class PoloFornecedor {
    private Long id;
    private Long fornecedorId;
    private Long energiaId;

    private String endereco;
    private String nome;
    private Double latitude;
    private Double longitude;

    private Long capacidadeMaxima;
    private Long capacidadeNormal;

    private Fornecedor fornecedor;
    private Energia energia;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
