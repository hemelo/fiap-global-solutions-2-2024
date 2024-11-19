package org.global.console.services;

import org.global.console.dto.request.create.CreateComunidadeDto;
import org.global.console.dto.request.update.UpdateComunidadeDto;
import org.global.console.dto.response.ComunidadeResponse;
import org.global.console.model.Comunidade;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ComunidadeServiceTest {

    private ComunidadeService comunidadeService;

    @BeforeAll
    void setUp() {
        comunidadeService = ComunidadeService.getInstance();

        comunidadeService.getComunidadeRepository().truncate();
    }

    @AfterAll
    void tearDown() {
        comunidadeService.getComunidadeRepository().truncate();
    }

    @Test
    void testCreateComunidade() {
        Assertions.assertDoesNotThrow(() -> {
            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade A", "Descrição A", "Localização A", 10.0, 20.0, 1000L);
            Comunidade comunidade = comunidadeService.createComunidade(createComunidadeDto);

            assertNotNull(comunidade);
            assertNotNull(comunidade.getId());
            assertEquals(createComunidadeDto.nome(), comunidade.getNome());
            assertEquals(createComunidadeDto.localizacao(), comunidade.getLocalizacao());
            assertEquals(createComunidadeDto.descricao(), comunidade.getDescricao());
            assertEquals(createComunidadeDto.latitude(), comunidade.getLatitude());
            assertEquals(createComunidadeDto.longitude(), comunidade.getLongitude());
            assertEquals(createComunidadeDto.populacao(), comunidade.getPopulacao());
        });
    }

    @Test
    void testUpdateComunidade() {
        Assertions.assertDoesNotThrow(() -> {
            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade B", "Descrição B", "Localização B", 15.0, 25.0, 2000L);
            Comunidade createdComunidade = comunidadeService.createComunidade(createComunidadeDto);

            UpdateComunidadeDto updateComunidadeDto = new UpdateComunidadeDto(createdComunidade.getId(), "Comunidade B Atualizada", "Descrição B Atualizada", "Localização B Atualizada", 15.5, 25.5, 2500L);
            Comunidade updatedComunidade = comunidadeService.updateComunidade(updateComunidadeDto);

            assertNotNull(updatedComunidade);
            assertEquals(updateComunidadeDto.nome(), updatedComunidade.getNome());
            assertEquals(updateComunidadeDto.localizacao(), updatedComunidade.getLocalizacao());
            assertEquals(updateComunidadeDto.descricao(), updatedComunidade.getDescricao());
            assertEquals(updateComunidadeDto.latitude(), updatedComunidade.getLatitude());
            assertEquals(updateComunidadeDto.longitude(), updatedComunidade.getLongitude());
            assertEquals(updateComunidadeDto.populacao(), updatedComunidade.getPopulacao());
        });
    }

    @Test
    void testGetAllComunidades() {
        Assertions.assertDoesNotThrow(() -> {
            List<Comunidade> comunidades = comunidadeService.getAllComunidades();

            assertNotNull(comunidades);
            assertFalse(comunidades.isEmpty());
        });
    }

    @Test
    void testGetComunidadeById() {
        Assertions.assertDoesNotThrow(() -> {
            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade C", "Descrição C", "Localização C", 20.0, 30.0, 3000L);
            Comunidade createdComunidade = comunidadeService.createComunidade(createComunidadeDto);

            Comunidade foundComunidade = comunidadeService.getComunidadeById(createdComunidade.getId());

            assertNotNull(foundComunidade);
            assertEquals(createdComunidade.getId(), foundComunidade.getId());
        });
    }

    @Test
    void testViewAllComunidades() {
        Assertions.assertDoesNotThrow(() -> {
            List<ComunidadeResponse> comunidadeResponses = comunidadeService.viewAllComunidades();

            assertNotNull(comunidadeResponses);
            assertFalse(comunidadeResponses.isEmpty());
        });
    }

    @Test
    void testViewComunidade() {
        Assertions.assertDoesNotThrow(() -> {
            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade D", "Descrição D", "Localização D", 25.0, 35.0, 4000L);
            Comunidade createdComunidade = comunidadeService.createComunidade(createComunidadeDto);

            ComunidadeResponse comunidadeResponse = comunidadeService.viewComunidade(createdComunidade.getId());

            assertNotNull(comunidadeResponse);
            assertEquals(createdComunidade.getId(), comunidadeResponse.id());
            assertEquals(createdComunidade.getNome(), comunidadeResponse.nome());
            assertEquals(createdComunidade.getLocalizacao(), comunidadeResponse.localizacao());
            assertEquals(createdComunidade.getDescricao(), comunidadeResponse.descricao());
            assertEquals(createdComunidade.getLatitude(), comunidadeResponse.latitude());
            assertEquals(createdComunidade.getLongitude(), comunidadeResponse.longitude());
            assertEquals(createdComunidade.getPopulacao(), comunidadeResponse.populacao());
        });
    }

    @Test
    void testDeleteComunidade() {
        Assertions.assertDoesNotThrow(() -> {
            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade E", "Descrição E", "Localização E", 30.0, 40.0, 5000L);
            Comunidade createdComunidade = comunidadeService.createComunidade(createComunidadeDto);

            boolean result = comunidadeService.deleteComunidade(createdComunidade.getId());

            assertTrue(result);

            Comunidade deletedComunidade = comunidadeService.getComunidadeById(createdComunidade.getId());
            assertNull(deletedComunidade);
        });
    }
}