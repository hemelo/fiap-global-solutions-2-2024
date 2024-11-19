package org.global.console.services;

import lombok.Getter;
import org.global.console.dto.request.create.CreateEnergiaDto;
import org.global.console.dto.request.update.UpdateEnergiaDto;
import org.global.console.dto.response.EnergiaResponse;
import org.global.console.model.Energia;
import org.global.console.repository.EnergiaRepository;
import org.global.console.repository.LogRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnergiaService {
    @Getter
    private final EnergiaRepository energiaRepository;

    private final LogRepository logRepository;

    private static EnergiaService instance;

    private EnergiaService() {
        this.logRepository = LogRepository.getInstance();
        this.energiaRepository = EnergiaRepository.getInstance();
    }

    public static synchronized EnergiaService getInstance() {

        if (instance == null) {
            instance = new EnergiaService();
        }

        return instance;
    }

    // Service methods here using DTOs

    public Energia createEnergia(CreateEnergiaDto createEnergiaDto) throws SQLException {
        Energia energia = Energia.builder()
                .nome(createEnergiaDto.nome())
                .descricao(createEnergiaDto.descricao())
                .tipo(createEnergiaDto.tipo())
                //.unidade(createEnergiaDto.unidade())
                .build();

        energiaRepository.save(energia);
        return energia;
    }

    public Energia updateEnergia(UpdateEnergiaDto updateEnergiaDto) throws SQLException {
        Energia energia = Energia.builder()
                .id(updateEnergiaDto.id())
                .nome(updateEnergiaDto.nome())
                .descricao(updateEnergiaDto.descricao())
                .tipo(updateEnergiaDto.tipo())
               // .unidade(updateEnergiaDto.unidade())
                .build();

        energiaRepository.update(energia);
        return energia;
    }

    public List<Energia> getAllEnergias() throws SQLException {
        return energiaRepository.findAll();
    }

    public Energia getEnergiaById(Long id) throws SQLException {
        return energiaRepository.findById(id);
    }

    public boolean deleteEnergia(Long id) throws SQLException {
        return energiaRepository.delete(id);
    }

    public List<EnergiaResponse> viewAllEnergias() throws SQLException {
        return Objects.requireNonNullElse(energiaRepository.findAll(), new ArrayList<Energia>()).stream()
                .map(this::toEnergiaResponse)
                .toList();
    }

    public EnergiaResponse viewEnergia(long id) throws SQLException {
        Energia energia = energiaRepository.findById(id);
        return toEnergiaResponse(energia);
    }

    public EnergiaResponse toEnergiaResponse(Energia energia) {

        if (energia == null) {
            return null;
        }

        return new EnergiaResponse(
                energia.getId(),
                energia.getTipo(),
                energia.getNome(),
                energia.getDescricao(),
                energia.getCreatedAt(),
                energia.getUpdatedAt()
               // energia.getUnidade().name()
        );
    }
}