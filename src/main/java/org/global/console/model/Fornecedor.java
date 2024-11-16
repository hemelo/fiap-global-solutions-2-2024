package org.global.console.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {
    private Long id;
    private Long contatoId;
    private String nome;
    private String descricao;
    private String endereco;
    private String cnpj;

    private List<PoloFornecedor> polos;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}