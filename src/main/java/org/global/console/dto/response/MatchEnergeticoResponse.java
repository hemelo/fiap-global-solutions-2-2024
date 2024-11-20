package org.global.console.dto.response;

import lombok.*;

import java.util.List;

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
    private Long populacaoDeficitInicial;
    private Long capacidadeMaximaPoloRestante;
    private Long populacaoASerAtendida;
    private Double percentualPopulacaoAtendidaEmRelacaoPopulacaoTotal;
    private Double percentualPopulacaoAtendidaEmRelacaoAoDeficit;
    private Double percentualCapacidadePoloRestante;
    private Integer rankDistancia;
    private Integer rankSuprimentoDeficit;
}
