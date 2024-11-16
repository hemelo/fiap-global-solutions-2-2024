package org.global.console.dto;

import java.util.List;

public record Sessao (
    String username,
    String nome,
    String email
) {}