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
import org.global.console.services.FornecedorService;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FornecedorServiceTest {

    private EnergiaService energiaService;
    private FornecedorService fornecedorService;

    @BeforeAll
    public void setUp() {
        energiaService = EnergiaService.getInstance();
        fornecedorService = FornecedorService.getInstance();

        fornecedorService.getPoloFornecedorRepository().truncate();
        fornecedorService.getFornecedorRepository().truncate();
        energiaService.getEnergiaRepository().truncate();
    }

    @AfterAll
    public void tearDown() {
        fornecedorService.getPoloFornecedorRepository().truncate();
        fornecedorService.getFornecedorRepository().truncate();
        energiaService.getEnergiaRepository().truncate();
    }


    @Test
    public void testCreateFornecedor() throws SQLException {
        CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor A", "12345678901234", "Endereco A",  "Descricao A");
        Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

        assertNotNull(fornecedor);
        assertNotNull(fornecedor.getId());
        assertEquals("Fornecedor A", fornecedor.getNome());
        assertEquals("Endereco A", fornecedor.getEndereco());
        assertEquals("12345678901234", fornecedor.getCnpj());
    }

    @Test
    public void testUpdateFornecedor() throws SQLException {
        CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor B", "23456789012345", "Endereco B",  "Descricao B");
        Fornecedor createdFornecedor = fornecedorService.createFornecedor(createFornecedorDto);

        assertNotNull(createdFornecedor);
        assertNotNull(createdFornecedor.getId());

        UpdateFornecedorDto updateFornecedorDto = new UpdateFornecedorDto(createdFornecedor.getId(), "Fornecedor B Atualizado", "23456789012345", "Endereco B Atualizado", "Descricao B Atualizado");
        Fornecedor updatedFornecedor = fornecedorService.updateFornecedor(updateFornecedorDto);

        assertNotNull(updatedFornecedor);
        assertEquals(createdFornecedor.getId(), updatedFornecedor.getId());
        assertEquals("Fornecedor B Atualizado", updatedFornecedor.getNome());
        assertEquals("Endereco B Atualizado", updatedFornecedor.getEndereco());
        assertEquals("23456789012345", updatedFornecedor.getCnpj());
    }

    @Test
    public void testGetAllFornecedores() throws SQLException {
        List<Fornecedor> fornecedores = fornecedorService.getAllFornecedores();

        assertNotNull(fornecedores);
        assertFalse(fornecedores.isEmpty());
    }

    @Test
    public void testGetFornecedorById() throws SQLException {
        CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor C",  "34567890123456", "Endereco C","Descricao C");
        Fornecedor createdFornecedor = fornecedorService.createFornecedor(createFornecedorDto);

        Fornecedor foundFornecedor = fornecedorService.getFornecedorById(createdFornecedor.getId());

        assertNotNull(foundFornecedor);
        assertEquals(createdFornecedor.getId(), foundFornecedor.getId());
    }

    @Test
    public void testDeleteFornecedor() throws SQLException {
        CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor D", "45678901234567", "Endereco D", "Descricao D");
        Fornecedor createdFornecedor = fornecedorService.createFornecedor(createFornecedorDto);

        fornecedorService.deleteFornecedor(createdFornecedor.getId());

        Fornecedor deletedFornecedor = fornecedorService.getFornecedorById(createdFornecedor.getId());
        assertNull(deletedFornecedor);
    }

    @Test
    public void testCreatePoloFornecedor() throws SQLException {

        CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Solar", "Energia obtida a partir do sol", null,"Renovável");
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
        assertEquals("Polo A", polo.getNome());
        assertEquals("Endereco A", polo.getEndereco());
        assertEquals(fornecedor.getId(), polo.getFornecedorId());
        assertEquals(energia.getId(), polo.getEnergiaId());
        assertEquals(2000L, polo.getCapacidadeNormal());
        assertEquals(4000L, polo.getCapacidadeMaxima());
    }

    @Test
    public void testUpdatePoloFornecedor() throws SQLException {

        CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Eólica", "Energia obtida a partir dos ventos", null,"Renovável");
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
        assertEquals("Polo B Atualizado", updatedPolo.getNome());
        assertEquals("Endereco B Atualizado", updatedPolo.getEndereco());
    }

    @Test
    public void testGetAllPolosFornecedor() throws SQLException {
        List<PoloFornecedor> polos = fornecedorService.getAllPolosFornecedor();

        assertNotNull(polos);
        assertFalse(polos.isEmpty());
    }

    @Test
    public void testDeletePoloFornecedor() throws SQLException {

        CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia das ondas", "Energia obtida a partir das ondas", null,"Renovável");
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

        fornecedorService.deletePoloFornecedor(createdPolo.getId());

        PoloFornecedor deletedPolo = fornecedorService.getPolosFornecedorByFornecedorId(1L).stream().filter(polo -> polo.getId().equals(createdPolo.getId())).findFirst().orElse(null);
        assertNull(deletedPolo);
    }

    @Test
    public void testViewFornecedor() throws SQLException {
        CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor H", "56789312345678", "Endereco H", "Descricao H");
        Fornecedor createdFornecedor = fornecedorService.createFornecedor(createFornecedorDto);

        FornecedorResponse fornecedorResponse = fornecedorService.viewFornecedor(createdFornecedor.getId());

        assertNotNull(fornecedorResponse);
        assertEquals(createdFornecedor.getId(), fornecedorResponse.id());
    }

    @Test
    public void testViewPoloFornecedor() throws SQLException {

        CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Nuclear", "Energia obtida a partir de urânio", null,"Não renovável");
        Energia energia = energiaService.createEnergia(createEnergiaDto);

        assertNotNull(energia);
        assertNotNull(energia.getId());

        CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor I",  "4342341312332","Endereco I", "Descricao I");
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
    }

    @Test
    public void testViewAllFornecedores() throws SQLException {
        List<FornecedorResponse> fornecedorResponses = fornecedorService.viewAllFornecedores();

        assertNotNull(fornecedorResponses);
        assertFalse(fornecedorResponses.isEmpty());
    }

    @Test
    public void testViewAllPolosFornecedor() throws SQLException {
        List<PoloFornecedorResponse> poloResponses = fornecedorService.viewAllPolosFornecedor();

        assertNotNull(poloResponses);
        assertFalse(poloResponses.isEmpty());
    }
}