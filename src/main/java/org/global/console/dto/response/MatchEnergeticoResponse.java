package org.global.console.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchEnergeticoResponse {
    private Long poloId;
    private Long fornecedorId;
    private Long energiaId;
    private Long comunidadeId;
    private ComunidadeResponse comunidade;
    private PoloFornecedorResponse polo;
    private FornecedorResponse fornecedor;
    private EnergiaResponse energia;
    private Double distancia;
    private Long populacaoDeficit;
    private Long capacidadeMaximaPoloRestante;
    private Long populacaoASerAtendida;
    private Byte percentualPopulacaoAtendidaEmRelacaoPopulacaoTotal;
    private Byte percentualPopulacaoAtendidaEmRelacaoAoDeficit;
    private Byte percentualCapacidadePoloRestante;
}
