package org.global.console.model;

import lombok.*;

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class Contato {
    private Long id;
    private String telefone;
    private String email;
}
