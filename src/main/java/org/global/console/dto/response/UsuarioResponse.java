package org.global.console.dto.response;

import java.time.LocalDateTime;

public record UsuarioResponse (
    String login,
    String nome,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){
}
