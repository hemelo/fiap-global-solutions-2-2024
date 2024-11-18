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
public class EnergiaServiceTest {

    private EnergiaService energiaService;

    @BeforeAll
    public void setUp() {
        energiaService = EnergiaService.getInstance();

        energiaService.getEnergiaRepository().truncate();
    }

    @AfterAll
    public void tearDown() {
        energiaService.getEnergiaRepository().truncate();
    }

    @Test
    public void testCreateEnergia() throws SQLException {
        CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Solar", "Energia obtida a partir do sol", null,"Solar");
        Energia energia = energiaService.createEnergia(createEnergiaDto);

        assertNotNull(energia);
        assertNotNull(energia.getId());
        assertEquals("Energia Solar", energia.getNome());
        assertEquals("Energia obtida a partir do sol", energia.getDescricao());
        assertEquals("Solar", energia.getTipo());
    }

    @Test
    public void testUpdateEnergia() throws SQLException {
        CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Eólica", "Energia obtida a partir do vento", null, "Eólica");
        Energia createdEnergia = energiaService.createEnergia(createEnergiaDto);

        UpdateEnergiaDto updateEnergiaDto = new UpdateEnergiaDto(createdEnergia.getId(), "Energia Eólica Atualizada", "Energia obtida a partir do vento atualizada", null, "Eólica");
        Energia updatedEnergia = energiaService.updateEnergia(updateEnergiaDto);

        assertNotNull(updatedEnergia);
        assertEquals("Energia Eólica Atualizada", updatedEnergia.getNome());
        assertEquals("Energia obtida a partir do vento atualizada", updatedEnergia.getDescricao());
        assertEquals("Eólica", updatedEnergia.getTipo());
    }

    @Test
    public void testGetAllEnergias() throws SQLException {
        List<Energia> energias = energiaService.getAllEnergias();

        assertNotNull(energias);
        assertFalse(energias.isEmpty());
    }

    @Test
    public void testGetEnergiaById() throws SQLException {
        CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Hidrelétrica", "Energia obtida a partir da água", null, "Hidrelétrica");
        Energia createdEnergia = energiaService.createEnergia(createEnergiaDto);

        Energia foundEnergia = energiaService.getEnergiaById(createdEnergia.getId());

        assertNotNull(foundEnergia);
        assertEquals(createdEnergia.getId(), foundEnergia.getId());
    }

    @Test
    public void testViewAllEnergias() throws SQLException {
        List<EnergiaResponse> energiaResponses = energiaService.viewAllEnergias();

        assertNotNull(energiaResponses);
        assertFalse(energiaResponses.isEmpty());
    }

    @Test
    public void testViewEnergia() throws SQLException {
        CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Geotérmica", "Energia obtida a partir do calor da Terra", null,"Geotérmica");
        Energia createdEnergia = energiaService.createEnergia(createEnergiaDto);

        EnergiaResponse energiaResponse = energiaService.viewEnergia(createdEnergia.getId());

        assertNotNull(energiaResponse);
        assertEquals(createdEnergia.getId(), energiaResponse.id());
        assertEquals("Energia Geotérmica", energiaResponse.nome());
        assertEquals("Energia obtida a partir do calor da Terra", energiaResponse.descricao());
        assertEquals("Geotérmica", energiaResponse.tipo());
    }

    @Test
    public void testDeleteEnergia() throws SQLException {
        CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Nuclear", "Energia obtida a partir de reações nucleares", null,"Nuclear");
        Energia createdEnergia = energiaService.createEnergia(createEnergiaDto);

        energiaService.deleteEnergia(createdEnergia.getId());

        Energia deletedEnergia = energiaService.getEnergiaById(createdEnergia.getId());
        assertNull(deletedEnergia);
    }

}