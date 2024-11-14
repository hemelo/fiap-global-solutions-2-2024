package org.global.console.dto.request;

public record CreateUserDto(
        String nome,
        String email,
        String senha,
        String permissao
) {}
