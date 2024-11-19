package org.global.console.services;

import org.global.console.dto.request.create.CreateEnergiaDto;
import org.global.console.dto.request.create.CreateFornecedorDto;
import org.global.console.dto.request.create.CreatePoloFornecedorDto;
import org.global.console.dto.request.update.UpdateFornecedorDto;
import org.global.console.dto.request.update.UpdatePoloFornecedorDto;
import org.global.console.dto.response.FornecedorResponse;
import org.global.console.dto.response.PoloFornecedorResponse;
import org.global.console.model.Energia;
import org.global.console.model.Fornecedor;
import org.global.console.model.PoloFornecedor;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FornecedorServiceTest {

    private EnergiaService energiaService;
    private FornecedorService fornecedorService;

    @BeforeAll
    void setUp() {
        energiaService = EnergiaService.getInstance();
        fornecedorService = FornecedorService.getInstance();

        fornecedorService.getPoloFornecedorRepository().truncate();
        fornecedorService.getFornecedorRepository().truncate();
        energiaService.getEnergiaRepository().truncate();
    }

    @AfterAll
    void tearDown() {
        fornecedorService.getPoloFornecedorRepository().truncate();
        fornecedorService.getFornecedorRepository().truncate();
        energiaService.getEnergiaRepository().truncate();
    }


    @Test
    void testCreateFornecedor() {
        Assertions.assertDoesNotThrow(() -> {
            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor A", "12345678901234", "Endereco A", "Descricao A");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);
            assertNotNull(fornecedor.getId());
            assertEquals(createFornecedorDto.nome(), fornecedor.getNome());
            assertEquals(createFornecedorDto.endereco(), fornecedor.getEndereco());
            assertEquals(createFornecedorDto.cnpj(), fornecedor.getCnpj());
        });
    }

    @Test
    void testUpdateFornecedor() {
        Assertions.assertDoesNotThrow(() -> {
            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor B", "23456789012345", "Endereco B", "Descricao B");
            Fornecedor createdFornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(createdFornecedor);
            assertNotNull(createdFornecedor.getId());

            UpdateFornecedorDto updateFornecedorDto = new UpdateFornecedorDto(createdFornecedor.getId(), "Fornecedor B Atualizado", "23456789012345", "Endereco B Atualizado", "Descricao B Atualizado");
            Fornecedor updatedFornecedor = fornecedorService.updateFornecedor(updateFornecedorDto);

            assertNotNull(updatedFornecedor);
            assertEquals(updateFornecedorDto.id(), updatedFornecedor.getId());
            assertEquals(updateFornecedorDto.nome(), updatedFornecedor.getNome());
            assertEquals(updateFornecedorDto.endereco(), updatedFornecedor.getEndereco());
            assertEquals(updateFornecedorDto.cnpj(), updatedFornecedor.getCnpj());
        });
    }

    @Test
    void testGetAllFornecedores() {
        Assertions.assertDoesNotThrow(() -> {
            List<Fornecedor> fornecedores = fornecedorService.getAllFornecedores();

            assertNotNull(fornecedores);
            assertFalse(fornecedores.isEmpty());
        });
    }

    @Test
    void testGetFornecedorById() {
        Assertions.assertDoesNotThrow(() -> {
            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor C", "34567890123456", "Endereco C", "Descricao C");
            Fornecedor createdFornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            Fornecedor foundFornecedor = fornecedorService.getFornecedorById(createdFornecedor.getId());

            assertNotNull(foundFornecedor);
            assertEquals(createdFornecedor.getId(), foundFornecedor.getId());
        });
    }

    @Test
    void testDeleteFornecedor() {
        Assertions.assertDoesNotThrow(() -> {
            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor D", "45678901234567", "Endereco D", "Descricao D");
            Fornecedor createdFornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            Boolean result = fornecedorService.deleteFornecedor(createdFornecedor.getId());

            assertTrue(result);

            Fornecedor deletedFornecedor = fornecedorService.getFornecedorById(createdFornecedor.getId());
            assertNull(deletedFornecedor);
        });
    }

    @Test
    void testCreatePoloFornecedor()  {
        Assertions.assertDoesNotThrow(() -> {

            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Solar", "Energia obtida a partir do sol", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);
            assertNotNull(energia.getId());

            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor E", "21312231312", "Endereco E", "Descricao E");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);
            assertNotNull(fornecedor.getId());

            CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto("Polo A", "Endereco A", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 4000L);
            PoloFornecedor polo = fornecedorService.createPoloFornecedor(createPoloFornecedorDto);

            assertNotNull(polo);
            assertNotNull(polo.getId());
            assertEquals(createPoloFornecedorDto.nome(), polo.getNome());
            assertEquals(createPoloFornecedorDto.endereco(), polo.getEndereco());
            assertEquals(fornecedor.getId(), polo.getFornecedorId());
            assertEquals(energia.getId(), polo.getEnergiaId());
            assertEquals(createPoloFornecedorDto.capacidadePopulacao(), polo.getCapacidadeNormal());
            assertEquals(createPoloFornecedorDto.capacidadePopulacaoMaxima(), polo.getCapacidadeMaxima());
        });
    }

    @Test
    void testUpdatePoloFornecedor() {

        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Eólica", "Energia obtida a partir dos ventos", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);
            assertNotNull(energia.getId());

            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor F", "32132142132", "Endereco F", "Descricao F");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);
            assertNotNull(fornecedor.getId());

            CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto("Polo B", "Endereco B", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 4000L);
            PoloFornecedor createdPolo = fornecedorService.createPoloFornecedor(createPoloFornecedorDto);

            assertNotNull(createdPolo);
            assertNotNull(createdPolo.getId());

            UpdatePoloFornecedorDto updatePoloFornecedorDto = new UpdatePoloFornecedorDto(createdPolo.getId(), "Polo B Atualizado", "Endereco B Atualizado", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 4000L);
            fornecedorService.updatePoloFornecedor(updatePoloFornecedorDto);

            PoloFornecedor updatedPolo = fornecedorService.getPoloFornecedorById(createdPolo.getId());

            assertNotNull(updatedPolo);
            assertEquals(updatePoloFornecedorDto.nome(), updatedPolo.getNome());
            assertEquals(updatePoloFornecedorDto.endereco(), updatedPolo.getEndereco());
            assertEquals(updatePoloFornecedorDto.fornecedorId(), updatedPolo.getFornecedorId());
            assertEquals(updatePoloFornecedorDto.energiaId(), updatedPolo.getEnergiaId());
            assertEquals(updatePoloFornecedorDto.capacidadePopulacao(), updatedPolo.getCapacidadeNormal());
            assertEquals(updatePoloFornecedorDto.capacidadePopulacaoMaxima(), updatedPolo.getCapacidadeMaxima());
        });
    }

    @Test
    void testGetAllPolosFornecedor() {
        Assertions.assertDoesNotThrow(() -> {
            List<PoloFornecedor> polos = fornecedorService.getAllPolosFornecedor();

            assertNotNull(polos);
            assertFalse(polos.isEmpty());
        });
    }

    @Test
    void testDeletePoloFornecedor() {

        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia das ondas", "Energia obtida a partir das ondas", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);
            assertNotNull(energia.getId());

            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor G", "12999678901234", "Endereco G", "Descricao G");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);
            assertNotNull(fornecedor.getId());

            CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto("Polo C", "Endereco C", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 4000L);
            PoloFornecedor createdPolo = fornecedorService.createPoloFornecedor(createPoloFornecedorDto);

            assertNotNull(createdPolo);
            assertNotNull(createdPolo.getId());

            boolean result = fornecedorService.deletePoloFornecedor(createdPolo.getId());

            assertTrue(result);

            PoloFornecedor deletedPolo = fornecedorService.getPolosFornecedorByFornecedorId(1L).stream().filter(polo -> polo.getId().equals(createdPolo.getId())).findFirst().orElse(null);
            assertNull(deletedPolo);
        });
    }

    @Test
    void testViewFornecedor() {
        Assertions.assertDoesNotThrow(() -> {
            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor H", "56789312345678", "Endereco H", "Descricao H");
            Fornecedor createdFornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            FornecedorResponse fornecedorResponse = fornecedorService.viewFornecedor(createdFornecedor.getId());

            assertNotNull(fornecedorResponse);
            assertEquals(createdFornecedor.getId(), fornecedorResponse.id());
        });
    }

    @Test
    void testViewPoloFornecedor() {

        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Nuclear", "Energia obtida a partir de urânio", null, "Não renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);
            assertNotNull(energia.getId());

            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor I", "4342341312332", "Endereco I", "Descricao I");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);
            assertNotNull(fornecedor.getId());

            CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto("Polo D", "Endereco D", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 4000L);
            fornecedorService.createPoloFornecedor(createPoloFornecedorDto);

            List<PoloFornecedor> polos = fornecedorService.getAllPolosFornecedor();
            PoloFornecedor createdPolo = polos.stream().filter(polo -> polo.getNome().equals("Polo D")).findFirst().orElse(null);
            assertNotNull(createdPolo);

            PoloFornecedorResponse poloResponse = fornecedorService.viewPoloFornecedor(createdPolo.getId());

            assertNotNull(poloResponse);
            assertEquals(createdPolo.getId(), poloResponse.id());
            assertEquals(createdPolo.getNome(), poloResponse.nome());
            assertEquals(createdPolo.getEndereco(), poloResponse.endereco());
            assertEquals(createdPolo.getFornecedorId(), poloResponse.idFornecedor());
            assertEquals(createdPolo.getEnergiaId(), poloResponse.idEnergia());
            assertEquals(createdPolo.getCapacidadeNormal(), poloResponse.capacidadePopulacao());
            assertEquals(createdPolo.getCapacidadeMaxima(), poloResponse.capacidadePopulacaoMaxima());
        });
    }

    @Test
    void testViewAllFornecedores()  {

        Assertions.assertDoesNotThrow(() -> {
            List<FornecedorResponse> fornecedorResponses = fornecedorService.viewAllFornecedores();

            assertNotNull(fornecedorResponses);
            assertFalse(fornecedorResponses.isEmpty());
        });
    }

    @Test
    void testViewAllPolosFornecedor() {

        Assertions.assertDoesNotThrow(() -> {

            List<PoloFornecedorResponse> poloResponses = fornecedorService.viewAllPolosFornecedor();

            assertNotNull(poloResponses);
            assertFalse(poloResponses.isEmpty());
        });
    }
}