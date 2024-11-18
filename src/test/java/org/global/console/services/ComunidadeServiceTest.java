package org.global.console.services;

import org.global.console.dto.request.create.CreateComunidadeDto;
import org.global.console.dto.request.update.UpdateComunidadeDto;
import org.global.console.dto.response.ComunidadeResponse;
import org.global.console.model.Comunidade;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ComunidadeServiceTest {

    private ComunidadeService comunidadeService;

    @BeforeAll
    public void setUp() {
        comunidadeService = ComunidadeService.getInstance();

        comunidadeService.getComunidadeRepository().truncate();
    }

    @AfterAll
    public void tearDown() {
        comunidadeService.getComunidadeRepository().truncate();
    }

    @Test
    public void testCreateComunidade() throws SQLException {
        CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade A", "Descrição A", "Localização A", 10.0, 20.0, 1000L);
        Comunidade comunidade = comunidadeService.createComunidade(createComunidadeDto);

        assertNotNull(comunidade);
        assertNotNull(comunidade.getId());
        assertEquals("Comunidade A", comunidade.getNome());
        assertEquals("Localização A", comunidade.getLocalizacao());
        assertEquals("Descrição A", comunidade.getDescricao());
        assertEquals(10.0, comunidade.getLatitude());
        assertEquals(20.0, comunidade.getLongitude());
        assertEquals(1000L, comunidade.getPopulacao());
    }

    @Test
    public void testUpdateComunidade() throws SQLException {
        CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade B", "Descrição B", "Localização B", 15.0, 25.0, 2000L);
        Comunidade createdComunidade = comunidadeService.createComunidade(createComunidadeDto);

        UpdateComunidadeDto updateComunidadeDto = new UpdateComunidadeDto(createdComunidade.getId(), "Comunidade B Atualizada", "Localização B Atualizada", "Descrição B Atualizada", 15.5, 25.5, 2500L);
        Comunidade updatedComunidade = comunidadeService.updateComunidade(updateComunidadeDto);

        assertNotNull(updatedComunidade);
        assertEquals("Comunidade B Atualizada", updatedComunidade.getNome());
        assertEquals("Localização B Atualizada", updatedComunidade.getLocalizacao());
        assertEquals("Descrição B Atualizada", updatedComunidade.getDescricao());
        assertEquals(15.5, updatedComunidade.getLatitude());
        assertEquals(25.5, updatedComunidade.getLongitude());
        assertEquals(2500L, updatedComunidade.getPopulacao());
    }

    @Test
    public void testGetAllComunidades() throws SQLException {
        List<Comunidade> comunidades = comunidadeService.getAllComunidades();

        assertNotNull(comunidades);
        assertFalse(comunidades.isEmpty());
    }

    @Test
    public void testGetComunidadeById() throws SQLException {
        CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade C", "Descrição C", "Localização C", 20.0, 30.0, 3000L);
        Comunidade createdComunidade = comunidadeService.createComunidade(createComunidadeDto);

        Comunidade foundComunidade = comunidadeService.getComunidadeById(createdComunidade.getId());

        assertNotNull(foundComunidade);
        assertEquals(createdComunidade.getId(), foundComunidade.getId());
    }

    @Test
    public void testViewAllComunidades() throws SQLException {
        List<ComunidadeResponse> comunidadeResponses = comunidadeService.viewAllComunidades();

        assertNotNull(comunidadeResponses);
        assertFalse(comunidadeResponses.isEmpty());
    }

    @Test
    public void testViewComunidade() throws SQLException {
        CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade D", "Descrição D", "Localização D", 25.0, 35.0, 4000L);
        Comunidade createdComunidade = comunidadeService.createComunidade(createComunidadeDto);

        ComunidadeResponse comunidadeResponse = comunidadeService.viewComunidade(createdComunidade.getId());

        assertNotNull(comunidadeResponse);
        assertEquals(createdComunidade.getId(), comunidadeResponse.id());
        assertEquals("Comunidade D", comunidadeResponse.nome());
        assertEquals("Localização D", comunidadeResponse.localizacao());
        assertEquals("Descrição D", comunidadeResponse.descricao());
        assertEquals(25.0, comunidadeResponse.latitude());
        assertEquals(35.0, comunidadeResponse.longitude());
        assertEquals(4000L, comunidadeResponse.populacao());
    }

    @Test
    public void testDeleteComunidade() throws SQLException {
        CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade E", "Descrição E", "Localização E", 30.0, 40.0, 5000L);
        Comunidade createdComunidade = comunidadeService.createComunidade(createComunidadeDto);

        comunidadeService.deleteComunidade(createdComunidade.getId());

        Comunidade deletedComunidade = comunidadeService.getComunidadeById(createdComunidade.getId());
        assertNull(deletedComunidade);
    }
}