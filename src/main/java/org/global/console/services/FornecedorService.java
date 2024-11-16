package org.global.console.services;

import org.global.console.dto.request.create.CreateFornecedorDto;
import org.global.console.dto.request.create.CreatePoloFornecedorDto;
import org.global.console.dto.request.update.UpdateFornecedorDto;
import org.global.console.dto.request.update.UpdatePoloFornecedorDto;
import org.global.console.dto.response.FornecedorResponse;
import org.global.console.dto.response.PoloFornecedorResponse;
import org.global.console.model.Fornecedor;
import org.global.console.model.PoloFornecedor;
import org.global.console.repository.FornecedorRepository;
import org.global.console.repository.LogRepository;
import org.global.console.repository.PoloFornecedorRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorService {
    private final FornecedorRepository fornecedorRepository;
    private static PoloFornecedorRepository poloFornecedorRepository;
    private final LogRepository logRepository;

    private static FornecedorService instance;

    private FornecedorService() {
        this.logRepository = LogRepository.getInstance();
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

    public void deleteFornecedor(Long id) throws SQLException {
        fornecedorRepository.delete(id);
    }

    public void createPoloFornecedor(CreatePoloFornecedorDto createPoloFornecedorDto) throws SQLException {

        PoloFornecedor poloFornecedor = PoloFornecedor.builder()
                .nome(createPoloFornecedorDto.nome())
                .endereco(createPoloFornecedorDto.endereco())
                .fornecedorId(createPoloFornecedorDto.fornecedorId())
                .latitude(createPoloFornecedorDto.latitude())
                .longitude(createPoloFornecedorDto.longitude())
                .build();


        poloFornecedorRepository.save(poloFornecedor);
    }

    public void updatePoloFornecedor(UpdatePoloFornecedorDto updatePoloFornecedorDto) throws SQLException {
        PoloFornecedor poloFornecedor = PoloFornecedor.builder()
                .id(updatePoloFornecedorDto.id())
                .nome(updatePoloFornecedorDto.nome())
                .endereco(updatePoloFornecedorDto.endereco())
                .fornecedorId(updatePoloFornecedorDto.fornecedorId())
                .latitude(updatePoloFornecedorDto.latitude())
                .longitude(updatePoloFornecedorDto.longitude())
                .build();

        poloFornecedorRepository.update(poloFornecedor);
    }

    public List<PoloFornecedor> getAllPolosFornecedor() throws SQLException {
        return poloFornecedorRepository.findAll();
    }

    public List<PoloFornecedor> getPolosFornecedorByFornecedorId(Long fornecedorId) throws SQLException {
        return poloFornecedorRepository.findByFornecedorId(fornecedorId);
    }

    public void deletePoloFornecedor(Long id) throws SQLException {
        poloFornecedorRepository.delete(id);
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

    private FornecedorResponse toResponse(Fornecedor fornecedor) {

        List<PoloFornecedorResponse> listPolos = new ArrayList<>();

        if (fornecedor.getPolos() != null) {
            for (PoloFornecedor polo : fornecedor.getPolos()) {
                listPolos.add(poloToResponse(polo));
            }
        }

        return new FornecedorResponse(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getEndereco(),
                fornecedor.getCnpj(),
                listPolos,
                fornecedor.getCreatedAt(),
                fornecedor.getUpdatedAt()
        );
    }

    private PoloFornecedorResponse poloToResponse(PoloFornecedor polo) {
        return new PoloFornecedorResponse(
                polo.getId(),
                polo.getNome(),
                polo.getLatitude(),
                polo.getLongitude(),
                polo.getEndereco(),
                polo.getFornecedorId(),
                polo.getCreatedAt(),
                polo.getUpdatedAt()
        );
    }


}