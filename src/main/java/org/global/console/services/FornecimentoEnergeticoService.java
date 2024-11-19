package org.global.console.services;

import lombok.Getter;
import org.global.console.dto.request.create.CreateFornecimentoEnergeticoDto;
import org.global.console.dto.request.update.UpdateFornecimentoEnergeticoDto;
import org.global.console.dto.response.*;
import org.global.console.model.FornecimentoEnergetico;
import org.global.console.model.PoloFornecedor;
import org.global.console.repository.FornecimentoEnergeticoRepository;
import org.global.console.repository.LogRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FornecimentoEnergeticoService {

    @Getter
    private final FornecimentoEnergeticoRepository fornecimentoEnergeticoRepository;

    private final LogRepository logRepository;

    private static FornecimentoEnergeticoService instance;

    private FornecimentoEnergeticoService() {
        this.logRepository = LogRepository.getInstance();
        this.fornecimentoEnergeticoRepository = FornecimentoEnergeticoRepository.getInstance();
    }

    public static FornecimentoEnergeticoService getInstance() {

        if (instance == null) {
            instance = new FornecimentoEnergeticoService();
        }

        return instance;
    }

    public FornecimentoEnergetico createFornecimentoEnergetico(CreateFornecimentoEnergeticoDto createFornecimentoEnergeticoDto) throws SQLException {
        FornecimentoEnergetico fornecimentoEnergetico = FornecimentoEnergetico.builder()
                .poloId(createFornecimentoEnergeticoDto.poloId())
                .comunidadeId(createFornecimentoEnergeticoDto.comunidadeId())
                .populacao(createFornecimentoEnergeticoDto.populacao())
                .build();

        fornecimentoEnergeticoRepository.save(fornecimentoEnergetico);
        return fornecimentoEnergetico;
    }

    public FornecimentoEnergetico updateFornecimentoEnergetico(UpdateFornecimentoEnergeticoDto updateFornecimentoEnergeticoDto) throws SQLException {
        FornecimentoEnergetico fornecimentoEnergetico = FornecimentoEnergetico.builder()
                .id(updateFornecimentoEnergeticoDto.id())
                .poloId(updateFornecimentoEnergeticoDto.poloId())
                .comunidadeId(updateFornecimentoEnergeticoDto.comunidadeId())
                .populacao(updateFornecimentoEnergeticoDto.populacao())
                .build();

        fornecimentoEnergeticoRepository.update(fornecimentoEnergetico);
        return fornecimentoEnergetico;
    }

    public List<FornecimentoEnergetico> getAllFornecimentoEnergeticos() throws SQLException {
        return fornecimentoEnergeticoRepository.findAll();
    }

    public List<FornecimentoEnergetico> getAllFornecimentoEnergeticosByComunidadeId(Long comunidadeId) throws SQLException {
        return fornecimentoEnergeticoRepository.findAllDetailedByComunidadeId(comunidadeId);
    }

    public List<FornecimentoEnergetico> getAllDetailedFornecimentoEnergeticos() throws SQLException {
        return fornecimentoEnergeticoRepository.findAllDetailed();
    }

    public List<FornecimentoEnergetico> getAllDetailedFornecimentoEnergeticosByComunidadeId(Long comunidadeId) throws SQLException {
        return fornecimentoEnergeticoRepository.findAllByComunidadeId(comunidadeId);
    }

    public FornecimentoEnergetico getFornecimentoEnergeticoById(Long id) throws SQLException {
        return fornecimentoEnergeticoRepository.findById(id);
    }

    public FornecimentoEnergetico getDetailedFornecimentoEnergeticoById(Long id) throws SQLException {
        return fornecimentoEnergeticoRepository.findByIdDetailed(id);
    }

    public boolean deleteFornecimentoEnergetico(Long id) throws SQLException {
        return fornecimentoEnergeticoRepository.delete(id);
    }

    public FornecimentoEnergeticoResponse viewFornecimentoEnergetico(long id) throws SQLException {
        FornecimentoEnergetico fornecimentoEnergetico = this.getDetailedFornecimentoEnergeticoById(id);
        return this.toResponse(fornecimentoEnergetico);
    }

    public List<FornecimentoEnergeticoResponse> viewAllFornecimentoEnergeticos() throws SQLException {
        List<FornecimentoEnergetico> fornecimentoEnergeticos = this.getAllDetailedFornecimentoEnergeticos();
        return Objects.requireNonNullElse(fornecimentoEnergeticos, new ArrayList<FornecimentoEnergetico>()).stream().map(this::toResponse).toList();
    }

    public List<FornecimentoEnergeticoResponse> viewAllFornecimentoEnergeticosByComunidadeId(Long id) throws SQLException {
        List<FornecimentoEnergetico> fornecimentoEnergeticos = this.getAllDetailedFornecimentoEnergeticosByComunidadeId(id);
        return Objects.requireNonNullElse(fornecimentoEnergeticos, new ArrayList<FornecimentoEnergetico>()).stream().map(this::toResponse).toList();
    }

    public FornecimentoEnergeticoResponse toResponse(FornecimentoEnergetico fornecimentoEnergetico) {

        ComunidadeResponse comunidadeResponse = ComunidadeService.getInstance().toResponse(fornecimentoEnergetico.getComunidade());
        PoloFornecedorResponse poloFornecedorResponse = FornecedorService.getInstance().poloToResponse(fornecimentoEnergetico.getPoloFornecedor());
        EnergiaResponse energiaResponse = EnergiaService.getInstance().toEnergiaResponse(Optional.ofNullable(fornecimentoEnergetico.getPoloFornecedor()).map(PoloFornecedor::getEnergia).orElse(null));
        FornecedorResponse fornecedorResponse = FornecedorService.getInstance().toResponse(Optional.ofNullable(fornecimentoEnergetico.getPoloFornecedor()).map(PoloFornecedor::getFornecedor).orElse(null));

        return new FornecimentoEnergeticoResponse(
                fornecimentoEnergetico.getId(),
                fornecimentoEnergetico.getComunidadeId(),
                fornecimentoEnergetico.getPoloId(),
                Optional.ofNullable(fornecedorResponse).map(FornecedorResponse::id).orElse(null),
                Optional.ofNullable(energiaResponse).map(EnergiaResponse::id).orElse(null),
                fornecimentoEnergetico.getPopulacao(),
                comunidadeResponse,
                poloFornecedorResponse,
                fornecedorResponse,
                energiaResponse,
                fornecimentoEnergetico.getCreatedAt(),
                fornecimentoEnergetico.getUpdatedAt()
        );
    }
}
