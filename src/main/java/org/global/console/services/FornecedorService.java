package org.global.console.services;

import lombok.Getter;
import org.global.console.dto.request.create.CreateFornecedorDto;
import org.global.console.dto.request.create.CreatePoloFornecedorDto;
import org.global.console.dto.request.update.UpdateFornecedorDto;
import org.global.console.dto.request.update.UpdatePoloFornecedorDto;
import org.global.console.dto.response.FornecedorResponse;
import org.global.console.dto.response.PoloFornecedorResponse;
import org.global.console.model.Fornecedor;
import org.global.console.model.FornecimentoEnergetico;
import org.global.console.model.PoloFornecedor;
import org.global.console.repository.FornecedorRepository;
import org.global.console.repository.LogRepository;
import org.global.console.repository.PoloFornecedorRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FornecedorService {

    @Getter
    private final FornecedorRepository fornecedorRepository;

    @Getter
    private final PoloFornecedorRepository poloFornecedorRepository;
    private final LogRepository logRepository;

    private static FornecedorService instance;

    private FornecedorService() {
        this.logRepository = LogRepository.getInstance();
        this.poloFornecedorRepository = PoloFornecedorRepository.getInstance();
        this.fornecedorRepository = FornecedorRepository.getInstance();
    }

    public static synchronized FornecedorService getInstance() {

        if (instance == null) {
            instance = new FornecedorService();
        }

        return instance;
    }

    // Service methods here using DTOs

    public Fornecedor createFornecedor(CreateFornecedorDto createFornecedorDto) throws SQLException {
        Fornecedor fornecedor = Fornecedor.builder()
                .nome(createFornecedorDto.nome())
                .endereco(createFornecedorDto.endereco())
                .descricao(createFornecedorDto.descricao())
                .cnpj(createFornecedorDto.cnpj())
                .build();

        fornecedorRepository.save(fornecedor);
        return fornecedor;
    }

    public Fornecedor updateFornecedor(UpdateFornecedorDto updateFornecedorDto) throws SQLException {
        Fornecedor fornecedor = Fornecedor.builder()
                .id(updateFornecedorDto.id())
                .nome(updateFornecedorDto.nome())
                .endereco(updateFornecedorDto.endereco())
                .descricao(updateFornecedorDto.descricao())
                .cnpj(updateFornecedorDto.cnpj())
                .build();

        fornecedorRepository.update(fornecedor);
        return fornecedor;
    }

    public List<Fornecedor> getAllFornecedores() throws SQLException {
        return fornecedorRepository.findAll();
    }

    public Fornecedor getFornecedorById(Long id) throws SQLException {
        return fornecedorRepository.findById(id);
    }

    public Boolean deleteFornecedor(Long id) throws SQLException {
         return fornecedorRepository.delete(id);
    }

    public PoloFornecedor createPoloFornecedor(CreatePoloFornecedorDto createPoloFornecedorDto) throws SQLException {

        PoloFornecedor poloFornecedor = PoloFornecedor.builder()
                .nome(createPoloFornecedorDto.nome())
                .endereco(createPoloFornecedorDto.endereco())
                .energiaId(createPoloFornecedorDto.energiaId())
                .fornecedorId(createPoloFornecedorDto.fornecedorId())
                .capacidadeNormal(createPoloFornecedorDto.capacidadePopulacao())
                .capacidadeMaxima(createPoloFornecedorDto.capacidadePopulacaoMaxima())
                .latitude(createPoloFornecedorDto.latitude())
                .longitude(createPoloFornecedorDto.longitude())
                .build();


        return poloFornecedorRepository.save(poloFornecedor);
    }

    public void updatePoloFornecedor(UpdatePoloFornecedorDto updatePoloFornecedorDto) throws SQLException {
        PoloFornecedor poloFornecedor = PoloFornecedor.builder()
                .id(updatePoloFornecedorDto.id())
                .nome(updatePoloFornecedorDto.nome())
                .endereco(updatePoloFornecedorDto.endereco())
                .fornecedorId(updatePoloFornecedorDto.fornecedorId())
                .energiaId(updatePoloFornecedorDto.energiaId())
                .capacidadeNormal(updatePoloFornecedorDto.capacidadePopulacao())
                .capacidadeMaxima(updatePoloFornecedorDto.capacidadePopulacaoMaxima())
                .latitude(updatePoloFornecedorDto.latitude())
                .longitude(updatePoloFornecedorDto.longitude())
                .build();

        poloFornecedorRepository.update(poloFornecedor);
    }


    public PoloFornecedor getPoloFornecedorById(Long id) throws SQLException {
        return poloFornecedorRepository.findById(id);
    }

    public List<PoloFornecedor> getAllPolosFornecedor() throws SQLException {
        return poloFornecedorRepository.findAll();
    }

    public List<PoloFornecedor> getPolosFornecedorByFornecedorId(Long fornecedorId) throws SQLException {
        return poloFornecedorRepository.findByFornecedorId(fornecedorId);
    }

    public boolean deletePoloFornecedor(Long id) throws SQLException {
        return poloFornecedorRepository.delete(id);
    }

    public FornecedorResponse viewFornecedor(Long id) throws SQLException {
        Fornecedor fornecedor = getFornecedorById(id);
        return toResponse(fornecedor);
    }

    public PoloFornecedorResponse viewPoloFornecedor(Long id) throws SQLException {
        PoloFornecedor poloFornecedor = poloFornecedorRepository.findById(id);
        return poloToResponse(poloFornecedor);
    }

    public List<FornecedorResponse> viewAllFornecedores() throws SQLException {
        List<Fornecedor> fornecedores = getAllFornecedores();
        List<FornecedorResponse> fornecedorResponses = new ArrayList<>();

        for (Fornecedor fornecedor : fornecedores) {
            fornecedorResponses.add(toResponse(fornecedor));
        }

        return fornecedorResponses;
    }

    public List<PoloFornecedorResponse> viewAllPolosFornecedor() throws SQLException {
        List<PoloFornecedor> polosFornecedor = getAllPolosFornecedor();
        List<PoloFornecedorResponse> poloFornecedorResponses = new ArrayList<>();

        for (PoloFornecedor poloFornecedor : polosFornecedor) {
            poloFornecedorResponses.add(poloToResponse(poloFornecedor));
        }

        return poloFornecedorResponses;
    }

    public long getSomaFornecimentoEnergeticoPolo(PoloFornecedor polo, List<FornecimentoEnergetico> fornecimentoEnergeticoList) {
        return Objects.requireNonNullElse(fornecimentoEnergeticoList, new ArrayList<FornecimentoEnergetico>()).stream().filter(fornecimento -> fornecimento.getPoloId().equals(polo.getId())).mapToLong(FornecimentoEnergetico::getPopulacao).sum();
    }

    FornecedorResponse toResponse(Fornecedor fornecedor) {

        if (fornecedor == null) {
            return null;
        }

        List<PoloFornecedorResponse> listPolos = new ArrayList<>();

        if (fornecedor.getPolos() != null) {
            for (PoloFornecedor polo : fornecedor.getPolos()) {
                listPolos.add(poloToResponse(polo));
            }
        }

        return new FornecedorResponse(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getCnpj(),
                fornecedor.getDescricao(),
                fornecedor.getEndereco(),
                listPolos,
                fornecedor.getCreatedAt(),
                fornecedor.getUpdatedAt()
        );
    }

    PoloFornecedorResponse poloToResponse(PoloFornecedor polo) {

        if (polo == null) {
            return null;
        }

        return new PoloFornecedorResponse(
                polo.getId(),
                polo.getNome(),
                polo.getLatitude(),
                polo.getLongitude(),
                polo.getEndereco(),
                polo.getFornecedorId(),
                polo.getEnergiaId(),
                polo.getCapacidadeNormal(),
                polo.getCapacidadeMaxima(),
                polo.getCreatedAt(),
                polo.getUpdatedAt()
        );
    }
}