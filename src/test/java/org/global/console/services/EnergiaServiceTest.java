package org.global.console.services;

import org.global.console.dto.request.create.CreateEnergiaDto;
import org.global.console.dto.request.update.UpdateEnergiaDto;
import org.global.console.dto.response.EnergiaResponse;
import org.global.console.infra.DataSource;
import org.global.console.model.Energia;
import org.global.console.services.EnergiaService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EnergiaServiceTest {

    private EnergiaService energiaService;

    @BeforeAll
    void setUp() {
        energiaService = EnergiaService.getInstance();

        energiaService.getEnergiaRepository().truncate();
    }

    @AfterAll
    void tearDown() {
        energiaService.getEnergiaRepository().truncate();
    }

    @Test
    void testCreateEnergia() {
        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Solar", "Energia obtida a partir do sol", null, "Solar");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);
            assertNotNull(energia.getId());
            assertEquals(createEnergiaDto.nome(), energia.getNome());
            assertEquals(createEnergiaDto.descricao(), energia.getDescricao());
            assertEquals(createEnergiaDto.tipo(), energia.getTipo());
        });
    }

    @Test
    void testUpdateEnergia() {
        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Eólica", "Energia obtida a partir do vento", null, "Eólica");
            Energia createdEnergia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(createdEnergia);
            assertNotNull(createdEnergia.getId());

            UpdateEnergiaDto updateEnergiaDto = new UpdateEnergiaDto(createdEnergia.getId(), "Energia Eólica Atualizada", "Energia obtida a partir do vento atualizada", null, "Eólica");
            Energia updatedEnergia = energiaService.updateEnergia(updateEnergiaDto);

            assertNotNull(updatedEnergia);
            assertEquals(updateEnergiaDto.nome(), updatedEnergia.getNome());
            assertEquals(updateEnergiaDto.descricao(), updatedEnergia.getDescricao());
            assertEquals(updateEnergiaDto.tipo(), updatedEnergia.getTipo());
        });
    }

    @Test
    void testGetAllEnergias() {
        Assertions.assertDoesNotThrow(() -> {
            List<Energia> energias = energiaService.getAllEnergias();

            assertNotNull(energias);
            assertFalse(energias.isEmpty());
        });
    }

    @Test
    void testGetEnergiaById() {
        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Hidrelétrica", "Energia obtida a partir da água", null, "Hidrelétrica");
            Energia createdEnergia = energiaService.createEnergia(createEnergiaDto);

            Energia foundEnergia = energiaService.getEnergiaById(createdEnergia.getId());

            assertNotNull(foundEnergia);
            assertEquals(createdEnergia.getId(), foundEnergia.getId());
        });
    }

    @Test
    void testViewAllEnergias() {
        Assertions.assertDoesNotThrow(() -> {
            List<EnergiaResponse> energiaResponses = energiaService.viewAllEnergias();

            assertNotNull(energiaResponses);
            assertFalse(energiaResponses.isEmpty());
        });
    }

    @Test
    void testViewEnergia() {
        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Geotérmica", "Energia obtida a partir do calor da Terra", null, "Geotérmica");
            Energia createdEnergia = energiaService.createEnergia(createEnergiaDto);

            EnergiaResponse energiaResponse = energiaService.viewEnergia(createdEnergia.getId());

            assertNotNull(energiaResponse);
            assertEquals(createdEnergia.getId(), energiaResponse.id());
            assertEquals(createdEnergia.getNome(), energiaResponse.nome());
            assertEquals(createdEnergia.getDescricao(), energiaResponse.descricao());
            assertEquals(createdEnergia.getTipo(), energiaResponse.tipo());
        });
    }

    @Test
    void testDeleteEnergia() {

        Assertions.assertDoesNotThrow(() -> {
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Nuclear", "Energia obtida a partir de reações nucleares", null, "Nuclear");
            Energia createdEnergia = energiaService.createEnergia(createEnergiaDto);

            boolean result = energiaService.deleteEnergia(createdEnergia.getId());

            assertTrue(result);

            Energia deletedEnergia = energiaService.getEnergiaById(createdEnergia.getId());
            assertNull(deletedEnergia);
        });
    }

}