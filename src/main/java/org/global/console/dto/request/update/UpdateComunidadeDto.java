package org.global.console.dto.request.update;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateComunidadeDto (
        @NotNull(message = "ID é obrigatório")
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotBlank(message = "Localização é obrigatória")
        String localizacao,

        @NotNull(message = "A latitude é obrigatória")
        @DecimalMin(value = "-90.0", message = "A latitude deve ser maior ou igual a -90.0")
        @DecimalMax(value = "90.0", message = "A latitude deve ser menor ou igual a 90.0")
        Double latitude,

        @NotNull(message = "A longitude é obrigatória")
        @DecimalMin(value = "-180.0", message = "A longitude deve ser maior ou igual a -180.0")
        @DecimalMax(value = "180.0", message = "A longitude deve ser menor ou igual a 180.0")
        Double longitude,

        @NotNull(message = "A população é obrigatória")
        @DecimalMin(value = "0", message = "A população deve ser maior ou igual a 0")
        Long populacao
) {}
