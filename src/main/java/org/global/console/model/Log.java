package org.global.console.model;

import lombok.*;

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class Log {

    private Long id;
    private String tipo;
    private String descricao;
    private Long usuarioId;
    private String objectId;
    private String objectClass;

    private Long createdAt;
    private Long updatedAt;
}
