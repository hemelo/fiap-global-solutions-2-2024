package org.global.console.services;

import org.global.console.dto.request.create.*;
import org.global.console.dto.request.update.UpdateFornecimentoEnergeticoDto;
import org.global.console.dto.response.FornecimentoEnergeticoResponse;
import org.global.console.model.*;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FornecimentoEnergeticoServiceTest {

    private ComunidadeService comunidadeService;
    private EnergiaService energiaService;
    private FornecedorService fornecedorService;
    private FornecimentoEnergeticoService fornecimentoEnergeticoService;

    @BeforeAll
    void setUp() {
        comunidadeService = ComunidadeService.getInstance();
        energiaService = EnergiaService.getInstance();
        fornecedorService = FornecedorService.getInstance();
        fornecimentoEnergeticoService = FornecimentoEnergeticoService.getInstance();

        fornecedorService.getPoloFornecedorRepository().truncate();
        fornecedorService.getFornecedorRepository().truncate();
        energiaService.getEnergiaRepository().truncate();
        fornecimentoEnergeticoService.getFornecimentoEnergeticoRepository().truncate();
        comunidadeService.getComunidadeRepository().truncate();
    }

    @AfterAll
    void tearDown() {
        fornecimentoEnergeticoService.getFornecimentoEnergeticoRepository().truncate();
        fornecedorService.getPoloFornecedorRepository().truncate();
        fornecedorService.getFornecedorRepository().truncate();
        energiaService.getEnergiaRepository().truncate();
        comunidadeService.getComunidadeRepository().truncate();
    }

    @Test
    void testCreateFornecimentoEnergetico() throws SQLException {

        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Solar", "Energia obtida a partir do sol", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);
            assertNotNull(energia.getId());

            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor A", "12345678901234", "Endereco A", "Descricao A");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);
            assertNotNull(fornecedor.getId());

            CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto("Polo A", "Endereco A", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 4000L);
            PoloFornecedor poloFornecedor = fornecedorService.createPoloFornecedor(createPoloFornecedorDto);

            assertNotNull(poloFornecedor);
            assertNotNull(poloFornecedor.getId());

            System.out.println(poloFornecedor.getId());

            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade A", "Endereco A", "Localizacao A", 10.0, 20.0, 2000L);
            Comunidade comunidade = comunidadeService.createComunidade(createComunidadeDto);

            assertNotNull(comunidade);
            assertNotNull(comunidade.getId());

            CreateFornecimentoEnergeticoDto createFornecimentoEnergeticoDto = new CreateFornecimentoEnergeticoDto(comunidade.getId(), poloFornecedor.getId(), 1000L);
            FornecimentoEnergetico fornecimentoEnergetico = fornecimentoEnergeticoService.createFornecimentoEnergetico(createFornecimentoEnergeticoDto);

            assertNotNull(fornecimentoEnergetico);
            assertNotNull(fornecimentoEnergetico.getId());
            assertEquals(comunidade.getId(), fornecimentoEnergetico.getComunidadeId());
            assertEquals(poloFornecedor.getId(), fornecimentoEnergetico.getPoloId());
            assertEquals(1000L, fornecimentoEnergetico.getPopulacao());
        });
    }

    @Test
    void testUpdateFornecimentoEnergetico() {

        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Eólica", "Energia obtida a partir dos ventos", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);
            assertNotNull(energia.getId());

            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor B", "23456789012345", "Endereco B", "Descricao B");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);
            assertNotNull(fornecedor.getId());

            CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto("Polo B", "Endereco B", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 4000L);
            PoloFornecedor poloFornecedor = fornecedorService.createPoloFornecedor(createPoloFornecedorDto);

            assertNotNull(poloFornecedor);
            assertNotNull(poloFornecedor.getId());

            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade B", "Endereco B", "Localizacao B", 20.0, 30.0, 3000L);
            Comunidade comunidade = comunidadeService.createComunidade(createComunidadeDto);

            assertNotNull(comunidade);
            assertNotNull(comunidade.getId());

            CreateFornecimentoEnergeticoDto createFornecimentoEnergeticoDto = new CreateFornecimentoEnergeticoDto(comunidade.getId(), poloFornecedor.getId(), 1500L);
            FornecimentoEnergetico fornecimentoEnergetico = fornecimentoEnergeticoService.createFornecimentoEnergetico(createFornecimentoEnergeticoDto);

            UpdateFornecimentoEnergeticoDto updateFornecimentoEnergeticoDto = new UpdateFornecimentoEnergeticoDto(fornecimentoEnergetico.getId(), comunidade.getId(), poloFornecedor.getId(), 2000L);
            FornecimentoEnergetico updatedFornecimentoEnergetico = fornecimentoEnergeticoService.updateFornecimentoEnergetico(updateFornecimentoEnergeticoDto);

            assertNotNull(updatedFornecimentoEnergetico);
            assertEquals(fornecimentoEnergetico.getId(), updatedFornecimentoEnergetico.getId());
            assertEquals(comunidade.getId(), updatedFornecimentoEnergetico.getComunidadeId());
            assertEquals(updateFornecimentoEnergeticoDto.id(), updatedFornecimentoEnergetico.getPoloId());
            assertEquals(updateFornecimentoEnergeticoDto.populacao(), updatedFornecimentoEnergetico.getPopulacao());
        });
    }

    @Test
    void testGetAllFornecimentosEnergeticos() throws SQLException {
        Assertions.assertDoesNotThrow(() -> {
            List<FornecimentoEnergetico> fornecimentosEnergeticos = fornecimentoEnergeticoService.getAllFornecimentoEnergeticos();

            assertNotNull(fornecimentosEnergeticos);
            assertFalse(fornecimentosEnergeticos.isEmpty());
        });
    }

    @Test
    void testGetFornecimentoEnergeticoById() throws SQLException {

        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Hidrelétrica", "Energia obtida a partir da água", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);
            assertNotNull(energia.getId());

            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor C", "34567890123456", "Endereco C", "Descricao C");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);
            assertNotNull(fornecedor.getId());

            CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto("Polo C", "Endereco C", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 4000L);
            PoloFornecedor poloFornecedor = fornecedorService.createPoloFornecedor(createPoloFornecedorDto);

            assertNotNull(poloFornecedor);
            assertNotNull(poloFornecedor.getId());

            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade C", "Endereco C", "Localizacao C", 10.0, 20.0, 2000L);
            Comunidade comunidade = comunidadeService.createComunidade(createComunidadeDto);

            assertNotNull(comunidade);
            assertNotNull(comunidade.getId());

            CreateFornecimentoEnergeticoDto createFornecimentoEnergeticoDto = new CreateFornecimentoEnergeticoDto(comunidade.getId(), poloFornecedor.getId(), 2500L);
            FornecimentoEnergetico fornecimentoEnergetico = fornecimentoEnergeticoService.createFornecimentoEnergetico(createFornecimentoEnergeticoDto);

            FornecimentoEnergetico foundFornecimentoEnergetico = fornecimentoEnergeticoService.getFornecimentoEnergeticoById(fornecimentoEnergetico.getId());

            assertNotNull(foundFornecimentoEnergetico);
            assertEquals(fornecimentoEnergetico.getId(), foundFornecimentoEnergetico.getId());
        });
    }

    @Test
    void testDeleteFornecimentoEnergetico() {
        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Geotérmica", "Energia obtida a partir do calor da Terra", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);
            assertNotNull(energia.getId());

            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor D", "45678901234567", "Endereco D", "Descricao D");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);
            assertNotNull(fornecedor.getId());

            CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto("Polo D", "Endereco D", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 4000L);
            PoloFornecedor poloFornecedor = fornecedorService.createPoloFornecedor(createPoloFornecedorDto);

            assertNotNull(poloFornecedor);
            assertNotNull(poloFornecedor.getId());

            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade D", "Endereco D", "Localizacao D", 10.0, 20.0, 2000L);
            Comunidade comunidade = comunidadeService.createComunidade(createComunidadeDto);

            assertNotNull(comunidade);
            assertNotNull(comunidade.getId());

            CreateFornecimentoEnergeticoDto createFornecimentoEnergeticoDto = new CreateFornecimentoEnergeticoDto(comunidade.getId(), poloFornecedor.getId(), 3000L);
            FornecimentoEnergetico fornecimentoEnergetico = fornecimentoEnergeticoService.createFornecimentoEnergetico(createFornecimentoEnergeticoDto);

            boolean result = fornecimentoEnergeticoService.deleteFornecimentoEnergetico(fornecimentoEnergetico.getId());

            assertTrue(result);

            FornecimentoEnergetico deletedFornecimentoEnergetico = fornecimentoEnergeticoService.getFornecimentoEnergeticoById(fornecimentoEnergetico.getId());
            assertNull(deletedFornecimentoEnergetico);
        });
    }

    @Test
    void testViewFornecimentoEnergetico() {
        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Biomassa", "Energia obtida a partir de matéria orgânica", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);
            assertNotNull(energia.getId());

            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor E", "56789012345678", "Endereco E", "Descricao E");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);
            assertNotNull(fornecedor.getId());

            CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto("Polo E", "Endereco E", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 4000L);
            PoloFornecedor poloFornecedor = fornecedorService.createPoloFornecedor(createPoloFornecedorDto);

            assertNotNull(poloFornecedor);
            assertNotNull(poloFornecedor.getId());

            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade E", "Endereco E", "Localizacao E", 10.0, 20.0, 2000L);
            Comunidade comunidade = comunidadeService.createComunidade(createComunidadeDto);

            assertNotNull(comunidade);
            assertNotNull(comunidade.getId());

            CreateFornecimentoEnergeticoDto createFornecimentoEnergeticoDto = new CreateFornecimentoEnergeticoDto(comunidade.getId(), poloFornecedor.getId(), 3500L);
            FornecimentoEnergetico fornecimentoEnergetico = fornecimentoEnergeticoService.createFornecimentoEnergetico(createFornecimentoEnergeticoDto);

            FornecimentoEnergeticoResponse fornecimentoEnergeticoResponse = fornecimentoEnergeticoService.viewFornecimentoEnergetico(fornecimentoEnergetico.getId());

            assertNotNull(fornecimentoEnergeticoResponse);
            assertEquals(fornecimentoEnergetico.getId(), fornecimentoEnergeticoResponse.id());
        });
    }

    @Test
    void testViewAllFornecimentosEnergeticos() {
        Assertions.assertDoesNotThrow(() -> {
            List<FornecimentoEnergeticoResponse> fornecimentoEnergeticoResponses = fornecimentoEnergeticoService.viewAllFornecimentoEnergeticos();

            assertNotNull(fornecimentoEnergeticoResponses);
            assertFalse(fornecimentoEnergeticoResponses.isEmpty());
        });
    }
}