package org.global.console.dto.request.create;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePoloFornecedorDto(
    @NotBlank(message = "O nome do polo fornecedor é obrigatório")
    String nome,

    @NotBlank(message = "O endereço do polo fornecedor é obrigatório")
    String endereco,

    @NotNull(message = "A latitude é obrigatória")
    @DecimalMin(value = "-90.0", message = "A latitude deve ser maior ou igual a -90.0")
    @DecimalMax(value = "90.0", message = "A latitude deve ser menor ou igual a 90.0")
    Double latitude,

    @NotNull(message = "A longitude é obrigatória")
    @DecimalMin(value = "-180.0", message = "A longitude deve ser maior ou igual a -180.0")
    @DecimalMax(value = "180.0", message = "A longitude deve ser menor ou igual a 180.0")
    Double longitude,

    @NotNull(message = "O ID do fornecedor é obrigatório")
    Long fornecedorId
) {

}
