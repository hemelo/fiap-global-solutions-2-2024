package org.global.console.services;

import org.global.console.dto.request.create.CreateConsumoDto;
import org.global.console.dto.request.update.UpdateConsumoDto;
import org.global.console.model.Consumo;
import org.global.console.repository.ConsumoRepository;
import org.global.console.repository.LogRepository;

import java.sql.SQLException;
import java.util.List;

public class ConsumoService {

    private final ConsumoRepository consumoRepository;
    private final LogRepository logRepository;

    private static ConsumoService instance;

    private ConsumoService() {
        this.logRepository = LogRepository.getInstance();
        this.consumoRepository = ConsumoRepository.getInstance();
    }

    public static ConsumoService getInstance() {

        if (instance == null) {
            instance = new ConsumoService();
        }

        return instance;
    }

    public Consumo createConsumo(CreateConsumoDto createConsumoDto) throws SQLException {
        Consumo consumo = Consumo.builder()
                .energiaId(createConsumoDto.energiaId())
                .comunidadeId(createConsumoDto.comunidadeId())
                .build();

        consumoRepository.save(consumo);
        return consumo;
    }

    public Consumo updateConsumo(UpdateConsumoDto updateConsumoDto) throws SQLException {
        Consumo consumo = Consumo.builder()
                .id(updateConsumoDto.id())
                .energiaId(updateConsumoDto.energiaId())
                .comunidadeId(updateConsumoDto.comunidadeId())
                .build();

        consumoRepository.update(consumo);
        return consumo;
    }

    public List<Consumo> getAllConsumos() throws SQLException {
        return consumoRepository.findAll();
    }

    public Consumo getConsumoById(Long id) throws SQLException {
        return consumoRepository.findById(id);
    }

    public void deleteConsumo(Long id) throws SQLException {
        consumoRepository.delete(id);
    }
}
