package org.global.console.dto.request;

public record UpdateUserDto(
        Long id,
        String nome,
        String email,
        String senha,
        String permissao
) {}

