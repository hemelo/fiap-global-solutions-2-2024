package org.global.console.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private String login;
    private String nome;
    private String email;
    private String senha;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}