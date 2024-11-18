package org.global.console.services;

import lombok.Getter;
import org.global.console.dto.request.create.CreateComunidadeDto;
import org.global.console.dto.request.update.UpdateComunidadeDto;
import org.global.console.dto.response.ComunidadeResponse;
import org.global.console.model.Comunidade;
import org.global.console.repository.ComunidadeRepository;
import org.global.console.repository.LogRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComunidadeService {

    @Getter
    private final ComunidadeRepository comunidadeRepository;

    private final LogRepository logRepository;

    private static ComunidadeService instance;

    private ComunidadeService() {
        this.logRepository = LogRepository.getInstance();
        this.comunidadeRepository = ComunidadeRepository.getInstance();
    }

    public static synchronized ComunidadeService getInstance() {

        if (instance == null) {
            instance = new ComunidadeService();
        }

        return instance;
    }

    public Comunidade createComunidade(CreateComunidadeDto createComunidadeDto) throws SQLException {
        Comunidade comunidade = Comunidade.builder()
                .populacao(createComunidadeDto.populacao())
                .nome(createComunidadeDto.nome())
                .localizacao(createComunidadeDto.localizacao())
                .descricao(createComunidadeDto.descricao())
                .latitude(createComunidadeDto.latitude())
                .longitude(createComunidadeDto.longitude())
                .build();

        comunidadeRepository.save(comunidade);

        return comunidade;
    }

    public Comunidade updateComunidade(UpdateComunidadeDto updateComunidadeDto) throws SQLException {
        Comunidade comunidade = Comunidade.builder()
                .populacao(updateComunidadeDto.populacao())
                .id(updateComunidadeDto.id())
                .nome(updateComunidadeDto.nome())
                .localizacao(updateComunidadeDto.localizacao())
                .descricao(updateComunidadeDto.descricao())
                .latitude(updateComunidadeDto.latitude())
                .longitude(updateComunidadeDto.longitude())
                .build();

        comunidadeRepository.update(comunidade);

        return comunidade;
    }

    public List<Comunidade> getAllComunidades() throws SQLException {
        return comunidadeRepository.findAll();
    }

    public Comunidade getComunidadeById(Long id) throws SQLException {
        return comunidadeRepository.findById(id);
    }

    public void deleteComunidade(Long id) throws SQLException {
        comunidadeRepository.delete(id);
    }

    public ComunidadeResponse viewComunidade(long id) throws SQLException {
        Comunidade comunidade = getComunidadeById(id);
        return toResponse(comunidade);
    }

    public List<ComunidadeResponse> viewAllComunidades() throws SQLException {
        List<Comunidade> comunidades = getAllComunidades();
        return Objects.requireNonNullElse(comunidades, new ArrayList<Comunidade>()).stream().map(this::toResponse).toList();
    }

    ComunidadeResponse toResponse(Comunidade comunidade) {

        if (comunidade == null) {
            return null;
        }

        return new ComunidadeResponse(
                comunidade.getId(),
                comunidade.getNome(),
                comunidade.getLocalizacao(),
                comunidade.getLatitude(),
                comunidade.getLongitude(),
                comunidade.getPopulacao(),
                comunidade.getDescricao(),
                comunidade.getCreatedAt(),
                comunidade.getUpdatedAt()
        );
    }

}