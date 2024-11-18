package org.global.console.dto.request.update;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePoloFornecedorDto(

        @NotNull(message = "ID é obrigatório")
        Long id,

        @NotBlank(message = "O nome do polo fornecedor é obrigatório")
        String nome,

        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @NotNull(message = "A latitude é obrigatória")
        @DecimalMin(value = "-90.0", message = "A latitude deve ser maior ou igual a -90.0")
        @DecimalMax(value = "90.0", message = "A latitude deve ser menor ou igual a 90.0")
        Double latitude,

        @NotNull(message = "A longitude é obrigatória")
        @DecimalMin(value = "-180.0", message = "A longitude deve ser maior ou igual a -180.0")
        Double longitude,

        @NotNull(message = "O ID do fornecedor é obrigatório")
        Long fornecedorId,

        @NotNull(message = "O ID da energia é obrigatório")
        Long energiaId,

        @DecimalMin(value = "0", message = "A capacidade de população deve ser maior ou igual a 0")
        @NotNull(message = "A capacidade de população é obrigatória")
        Long capacidadePopulacao,

        @DecimalMin(value = "0", message = "A capacidade de população máxima deve ser maior ou igual a 0")
        @NotNull(message = "A capacidade de população máxima é obrigatória")
        Long capacidadePopulacaoMaxima
) {

}
