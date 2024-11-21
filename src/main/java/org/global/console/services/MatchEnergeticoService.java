package org.global.console.services;

import org.global.console.dto.response.ComunidadeResponse;
import org.global.console.dto.response.MatchEnergeticoResponse;
import org.global.console.exceptions.NegocioException;
import org.global.console.exceptions.RecursoNaoEncontradoException;
import org.global.console.model.Comunidade;
import org.global.console.model.FornecimentoEnergetico;
import org.global.console.model.PoloFornecedor;
import org.global.console.utils.MathUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MatchEnergeticoService {

    private ComunidadeService comunidadeService;
    private FornecedorService fornecedorService;
    private FornecimentoEnergeticoService fornecimentoEnergeticoService;
    private static MatchEnergeticoService instance;

    private MatchEnergeticoService() {
        this.fornecimentoEnergeticoService = FornecimentoEnergeticoService.getInstance();
        this.comunidadeService = ComunidadeService.getInstance();
        this.fornecedorService = FornecedorService.getInstance();
    }

    public static MatchEnergeticoService getInstance() {

        if (instance == null) {
            instance = new MatchEnergeticoService();
        }

        return instance;
    }

    public List<MatchEnergeticoResponse> realizarMatchEnergetico(Long comunidadeId) throws SQLException {
        List<FornecimentoEnergetico> fornecimentoEnergeticoList = Objects.requireNonNullElse(fornecimentoEnergeticoService.getAllDetailedFornecimentoEnergeticos(), new ArrayList<>());
        Comunidade comunidade = comunidadeService.getComunidadeById(comunidadeId);

        if (comunidade == null) {
            throw new RecursoNaoEncontradoException("Comunidade não encontrada");
        }

        ComunidadeResponse comunidadeResponse = comunidadeService.toResponse(comunidade);

        long totalPopulacaoAtendida = fornecimentoEnergeticoList.stream().filter(f -> Objects.equals(f.getComunidadeId(), comunidadeId)).mapToLong(FornecimentoEnergetico::getPopulacao).sum();
        long populacaoComunidade = comunidade.getPopulacao();
        long populacaoNaoAtendida = populacaoComunidade - totalPopulacaoAtendida;

        if (populacaoNaoAtendida <= 0) {
            return null;
        }

        List<PoloFornecedor> poloFornecedorList = fornecedorService.getAllPolosFornecedor();

        if (poloFornecedorList.isEmpty()) {
            throw new RecursoNaoEncontradoException("Não há PoloFornecedor cadastrado");
        }

        // Filtrar os PoloFornecedor que ainda possuem capacidade de atendimento
        poloFornecedorList = Objects.requireNonNullElse(poloFornecedorList, new ArrayList<PoloFornecedor>()).stream().filter(polo -> {
            long populacaoAtendidaPolo = fornecedorService.getSomaFornecimentoEnergeticoPolo(polo, fornecimentoEnergeticoList);
            return populacaoAtendidaPolo < polo.getCapacidadeNormal();
        }).collect(Collectors.toList());

        // Ordenar os PoloFornecedor mais próximos da Comunidade
        poloFornecedorList = encontrarPoloFornecedorMaisProximos(comunidade, poloFornecedorList);

        if (poloFornecedorList.isEmpty()) {
            throw new NegocioException("Não há PoloFornecedor disponíveis para atender a Comunidade");
        }
        

        long aAtender = 0;
        long populacaoNaoAtendidaOriginal = populacaoNaoAtendida;
        List<MatchEnergeticoResponse> matches = new ArrayList<>();

        for (PoloFornecedor poloFornecedor : poloFornecedorList) {

            if (populacaoNaoAtendida <= 0) break;

            long populacaoAtendidaPolo = fornecedorService.getSomaFornecimentoEnergeticoPolo(poloFornecedor, fornecimentoEnergeticoList);
            long capacidadeRestante = poloFornecedor.getCapacidadeNormal() - populacaoAtendidaPolo;

            if (capacidadeRestante <= 0) continue;

            if (capacidadeRestante > populacaoNaoAtendida) {
                aAtender = populacaoNaoAtendida;
            } else {
                aAtender = capacidadeRestante;
            }

            MatchEnergeticoResponse match = new MatchEnergeticoResponse();
            match.setComunidadeId(comunidadeId);
            match.setFornecedorId(poloFornecedor.getFornecedorId());
            match.setEnergiaId(poloFornecedor.getEnergiaId());
            match.setPoloId(poloFornecedor.getId());
            match.setPopulacaoASerAtendida(aAtender);
            match.setDistancia(MathUtils.calcularDistancia(comunidade.getLatitude(), comunidade.getLongitude(), poloFornecedor.getLatitude(), poloFornecedor.getLongitude()));
            match.setComunidade(comunidadeResponse);
            match.setCapacidadeMaximaPoloRestante(capacidadeRestante - aAtender);
            match.setPercentualCapacidadePoloRestante((double) ((capacidadeRestante - aAtender) * 100.0 / poloFornecedor.getCapacidadeNormal()));
            match.setPercentualPopulacaoAtendidaEmRelacaoPopulacaoTotal((double) (aAtender * 100.0 / populacaoComunidade));
            match.setPercentualPopulacaoAtendidaEmRelacaoAoDeficit((double) (aAtender * 100.0 / populacaoNaoAtendidaOriginal));
            match.setPopulacaoDeficitInicial(populacaoNaoAtendidaOriginal);
            matches.add(match);

            populacaoNaoAtendida -= aAtender;
        }

        for (int i = 0; i < matches.size(); i++) {
            MatchEnergeticoResponse match = matches.get(i);

            // Calculate rank in distance
            long rankDistancia = matches.stream()
                    .filter(m -> !m.equals(match))
                    .filter(m -> m.getDistancia() < match.getDistancia())
                    .count() + 1;
            match.setRankDistancia((int) rankDistancia);

            // Calculate rank in deficit supply
            long rankSuprimentoDeficit = matches.stream()
                    .filter(m -> !m.equals(match))
                    .filter(m -> m.getPopulacaoASerAtendida() > match.getPopulacaoASerAtendida())
                    .count() + 1;
            match.setRankSuprimentoDeficit((int) rankSuprimentoDeficit);
        }

        return matches;
    }

    // Método para encontrar os PoloFornecedor mais próximos de uma Comunidade
    public List<PoloFornecedor> encontrarPoloFornecedorMaisProximos(Comunidade comunidade, List<PoloFornecedor> poloFornecedores) {
        return Objects.requireNonNullElse(poloFornecedores, new ArrayList<PoloFornecedor>()).stream()
                .sorted(Comparator.comparingDouble(polo -> MathUtils.calcularDistancia(
                        comunidade.getLatitude(), comunidade.getLongitude(),
                        polo.getLatitude(), polo.getLongitude())))
                .toList();
    }
}
