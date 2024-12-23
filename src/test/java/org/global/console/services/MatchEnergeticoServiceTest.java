package org.global.console.services;

import org.global.console.dto.request.create.*;
import org.global.console.dto.response.MatchEnergeticoResponse;
import org.global.console.exceptions.NegocioException;
import org.global.console.exceptions.RecursoNaoEncontradoException;
import org.global.console.model.*;
import org.global.console.utils.MathUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MatchEnergeticoServiceTest {

    private MatchEnergeticoService matchEnergeticoService;
    private ComunidadeService comunidadeService;
    private EnergiaService energiaService;
    private FornecedorService fornecedorService;
    private FornecimentoEnergeticoService fornecimentoEnergeticoService;

    @BeforeAll
    void setUp() {
        matchEnergeticoService = MatchEnergeticoService.getInstance();
        comunidadeService = ComunidadeService.getInstance();
        energiaService = EnergiaService.getInstance();
        fornecedorService = FornecedorService.getInstance();
        fornecimentoEnergeticoService = FornecimentoEnergeticoService.getInstance();

        comunidadeService.getComunidadeRepository().truncate();
        fornecedorService.getPoloFornecedorRepository().truncate();
        fornecedorService.getFornecedorRepository().truncate();
        energiaService.getEnergiaRepository().truncate();
        fornecimentoEnergeticoService.getFornecimentoEnergeticoRepository().truncate();
    }

    @BeforeEach
    void init() {
        comunidadeService.getComunidadeRepository().truncate();
        fornecedorService.getPoloFornecedorRepository().truncate();
        fornecedorService.getFornecedorRepository().truncate();
        energiaService.getEnergiaRepository().truncate();
        fornecimentoEnergeticoService.getFornecimentoEnergeticoRepository().truncate();
    }

    @AfterAll
    void tearDown() {
        comunidadeService.getComunidadeRepository().truncate();
        fornecedorService.getPoloFornecedorRepository().truncate();
        fornecedorService.getFornecedorRepository().truncate();
        energiaService.getEnergiaRepository().truncate();
        fornecimentoEnergeticoService.getFornecimentoEnergeticoRepository().truncate();
    }

    @Test
    void testRealizarMatchEnergetico() {

        Assertions.assertDoesNotThrow(() -> {

            // Criar Energia
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Solar", "Energia obtida a partir do sol", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            // Criar Fornecedor
            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor A", "12345678901234", "Endereco A", "Descricao A");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            // Criar PoloFornecedor
            CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto("Polo A", "Endereco A", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 4000L);
            PoloFornecedor poloFornecedor = fornecedorService.createPoloFornecedor(createPoloFornecedorDto);

            // Criar Comunidade
            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade A", "Localizacao A", "Descricao A", 20.0, 20.0, 3000L);
            Comunidade comunidade = comunidadeService.createComunidade(createComunidadeDto);

            // Realizar Match
            // Este match considera que o polo não fornece para mais nenhuma comunidade
            List<MatchEnergeticoResponse> matches = matchEnergeticoService.realizarMatchEnergetico(comunidade.getId());

            // Validar Matches
            assertNotNull(matches);
            assertFalse(matches.isEmpty());
            assertEquals(1, matches.size());

            MatchEnergeticoResponse match = matches.get(0);
            assertEquals(comunidade.getId(), match.getComunidadeId());
            assertEquals(poloFornecedor.getId(), match.getPoloId());
            assertEquals(poloFornecedor.getCapacidadeNormal(), match.getPopulacaoASerAtendida());
            assertEquals(energia.getId(), match.getEnergiaId());
            assertEquals(fornecedor.getId(), match.getFornecedorId());
            assertEquals(comunidade.getPopulacao(), match.getPopulacaoDeficitInicial());
            assertEquals(poloFornecedor.getCapacidadeNormal() - match.getPopulacaoASerAtendida(), match.getCapacidadeMaximaPoloRestante());
            assertEquals((poloFornecedor.getCapacidadeNormal() - match.getPopulacaoASerAtendida()) * 100.0 / poloFornecedor.getCapacidadeNormal(), match.getPercentualCapacidadePoloRestante());
            assertEquals(poloFornecedor.getCapacidadeNormal() * 100.0 / comunidade.getPopulacao(), match.getPercentualPopulacaoAtendidaEmRelacaoAoDeficit());
            assertEquals(MathUtils.calcularDistancia(comunidade.getLatitude(), comunidade.getLongitude(), poloFornecedor.getLatitude(), poloFornecedor.getLongitude()), match.getDistancia());

        });
    }

    @Test
    void testRealizarMatchEnergeticoComMultiplosPolos() {

        Assertions.assertDoesNotThrow(() -> {
            // Criar Energia
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Eólica", "Energia obtida a partir dos ventos", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);

            // Criar Fornecedor
            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor B", "23456789012345", "Endereco B", "Descricao B");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);

            // Criar PoloFornecedor 1
            // Este deve ser mais próximo da comunidade
            CreatePoloFornecedorDto createPoloFornecedorDto1 = new CreatePoloFornecedorDto("Polo B1", "Endereco B1", 15.0, 15.0, fornecedor.getId(), energia.getId(), 3000L, 3000L);
            PoloFornecedor poloFornecedor1 = fornecedorService.createPoloFornecedor(createPoloFornecedorDto1);

            assertNotNull(poloFornecedor1);

            // Criar PoloFornecedor 2
            CreatePoloFornecedorDto createPoloFornecedorDto2 = new CreatePoloFornecedorDto("Polo B1", "Endereco B1", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 2000L);
            PoloFornecedor poloFornecedor2 = fornecedorService.createPoloFornecedor(createPoloFornecedorDto2);

            assertNotNull(poloFornecedor2);

            // Criar Comunidade
            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade B", "Localizacao B", "Descricao B", 25.0, 25.0, 6000L);
            Comunidade comunidade = comunidadeService.createComunidade(createComunidadeDto);

            assertNotNull(comunidade);

            // Realizar Match
            List<MatchEnergeticoResponse> matches = matchEnergeticoService.realizarMatchEnergetico(comunidade.getId());

            // Validar Matches
            assertNotNull(matches);
            assertFalse(matches.isEmpty());
            assertEquals(2, matches.size());

            MatchEnergeticoResponse match1 = matches.get(0);
            assertEquals(comunidade.getId(), match1.getComunidadeId());
            assertEquals(poloFornecedor1.getId(), match1.getPoloId());
            assertEquals(poloFornecedor1.getCapacidadeNormal(), match1.getPopulacaoASerAtendida());
            assertEquals(energia.getId(), match1.getEnergiaId());
            assertEquals(fornecedor.getId(), match1.getFornecedorId());
            assertEquals(comunidade.getPopulacao(), match1.getPopulacaoDeficitInicial());
            assertEquals(poloFornecedor1.getCapacidadeNormal() - match1.getPopulacaoASerAtendida(), match1.getCapacidadeMaximaPoloRestante());
            assertEquals((poloFornecedor1.getCapacidadeNormal() - match1.getPopulacaoASerAtendida()) * 100.0 / poloFornecedor1.getCapacidadeNormal(), match1.getPercentualCapacidadePoloRestante());
            assertEquals(poloFornecedor1.getCapacidadeNormal() * 100.0 / comunidade.getPopulacao(), match1.getPercentualPopulacaoAtendidaEmRelacaoAoDeficit());
            assertEquals(MathUtils.calcularDistancia(comunidade.getLatitude(), comunidade.getLongitude(), poloFornecedor1.getLatitude(), poloFornecedor1.getLongitude()), match1.getDistancia());
            assertEquals(1, match1.getRankDistancia());

            MatchEnergeticoResponse match2 = matches.get(1);
            assertEquals(comunidade.getId(), match2.getComunidadeId());
            assertEquals(poloFornecedor2.getId(), match2.getPoloId());
            assertEquals(poloFornecedor2.getCapacidadeNormal(), match2.getPopulacaoASerAtendida());
            assertEquals(energia.getId(), match2.getEnergiaId());
            assertEquals(fornecedor.getId(), match2.getFornecedorId());
            assertEquals(comunidade.getPopulacao(), match2.getPopulacaoDeficitInicial());
            assertEquals(poloFornecedor2.getCapacidadeNormal() - match2.getPopulacaoASerAtendida(), match2.getCapacidadeMaximaPoloRestante());
            assertEquals((poloFornecedor2.getCapacidadeNormal() - match2.getPopulacaoASerAtendida()) * 100.0 / poloFornecedor2.getCapacidadeNormal(), match2.getPercentualCapacidadePoloRestante());
            assertEquals(poloFornecedor2.getCapacidadeNormal() * 100.0 / comunidade.getPopulacao(), match2.getPercentualPopulacaoAtendidaEmRelacaoAoDeficit());
            assertEquals(MathUtils.calcularDistancia(comunidade.getLatitude(), comunidade.getLongitude(), poloFornecedor2.getLatitude(), poloFornecedor2.getLongitude()), match2.getDistancia());
            assertEquals(2, match2.getRankDistancia());

            assertEquals(1, match1.getPopulacaoASerAtendida() > match2.getPopulacaoASerAtendida() ? match1.getRankSuprimentoDeficit() : match2.getRankSuprimentoDeficit());

            if (Objects.equals(match1.getPopulacaoASerAtendida(), match2.getPopulacaoASerAtendida())) {
                assertEquals(1, match1.getPopulacaoASerAtendida() < match2.getPopulacaoASerAtendida() ? match1.getRankSuprimentoDeficit() : match2.getRankSuprimentoDeficit());
            } else {
                assertEquals(2, match1.getPopulacaoASerAtendida() < match2.getPopulacaoASerAtendida() ? match1.getRankSuprimentoDeficit() : match2.getRankSuprimentoDeficit());
            }

            assertTrue(match1.getDistancia() < match2.getDistancia());
        });
    }

    @Test
    void testRealizarMatchEnergeticoComMultiplosPolosEmUso() {

        Assertions.assertDoesNotThrow(() -> {
            // Criar Energia
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Eólica", "Energia obtida a partir dos ventos", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);

            assertNotNull(energia);

            // Criar Fornecedor
            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor B", "23456789012345", "Endereco B", "Descricao B");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);

            assertNotNull(fornecedor);

            // Criar PoloFornecedor 1
            // Este deve ser mais próximo da comunidade
            CreatePoloFornecedorDto createPoloFornecedorDto1 = new CreatePoloFornecedorDto("Polo B1", "Endereco B1", 15.0, 15.0, fornecedor.getId(), energia.getId(), 3000L, 3000L);
            PoloFornecedor poloFornecedor1 = fornecedorService.createPoloFornecedor(createPoloFornecedorDto1);

            assertNotNull(poloFornecedor1);

            // Criar PoloFornecedor 2
            CreatePoloFornecedorDto createPoloFornecedorDto2 = new CreatePoloFornecedorDto("Polo B1", "Endereco B1", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 2000L);
            PoloFornecedor poloFornecedor2 = fornecedorService.createPoloFornecedor(createPoloFornecedorDto2);

            assertNotNull(poloFornecedor2);

            // Criar outra comunidade
            CreateComunidadeDto createComunidadeDto2 = new CreateComunidadeDto("Comunidade C", "Localizacao C", "Descricao C", 30.0, 30.0, 9000L);
            Comunidade comunidade2 = comunidadeService.createComunidade(createComunidadeDto2);

            assertNotNull(comunidade2);

            // Criar fornecimento energético para a comunidade 2
            CreateFornecimentoEnergeticoDto createFornecimentoEnergeticoDto = new CreateFornecimentoEnergeticoDto(comunidade2.getId(), poloFornecedor1.getId(), 1000L);
            FornecimentoEnergetico fornecimentoEnergetico = fornecimentoEnergeticoService.createFornecimentoEnergetico(createFornecimentoEnergeticoDto);

            assertNotNull(fornecimentoEnergetico);

            // Criar Comunidade
            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade B", "Localizacao B", "Descricao B", 25.0, 25.0, 6000L);
            Comunidade comunidade = comunidadeService.createComunidade(createComunidadeDto);

            assertNotNull(comunidade);

            // Realizar Match
            List<MatchEnergeticoResponse> matches = matchEnergeticoService.realizarMatchEnergetico(comunidade.getId());

            // Validar Matches
            assertNotNull(matches);
            assertFalse(matches.isEmpty());
            assertEquals(2, matches.size());

            long capacidadeRealPolo = poloFornecedor1.getCapacidadeNormal() - fornecedorService.getSomaFornecimentoEnergeticoPolo(poloFornecedor1, List.of(fornecimentoEnergetico));

            MatchEnergeticoResponse match1 = matches.get(0);
            assertEquals(comunidade.getId(), match1.getComunidadeId());
            assertEquals(poloFornecedor1.getId(), match1.getPoloId());
            assertEquals(capacidadeRealPolo, match1.getPopulacaoASerAtendida());
            assertNotEquals(poloFornecedor1.getCapacidadeNormal(), match1.getPopulacaoASerAtendida());
            assertEquals(energia.getId(), match1.getEnergiaId());
            assertEquals(fornecedor.getId(), match1.getFornecedorId());
            assertEquals(comunidade.getPopulacao(), match1.getPopulacaoDeficitInicial());
            assertNotEquals(poloFornecedor1.getCapacidadeNormal() - match1.getPopulacaoASerAtendida(), match1.getCapacidadeMaximaPoloRestante());
            assertNotEquals((poloFornecedor1.getCapacidadeNormal() - match1.getPopulacaoASerAtendida()) * 100.0 / poloFornecedor1.getCapacidadeNormal(), match1.getPercentualCapacidadePoloRestante());
            assertNotEquals(poloFornecedor1.getCapacidadeNormal() * 100.0 / comunidade.getPopulacao(), match1.getPercentualPopulacaoAtendidaEmRelacaoAoDeficit());
            assertEquals(capacidadeRealPolo - match1.getPopulacaoASerAtendida(), match1.getCapacidadeMaximaPoloRestante());
            assertEquals((capacidadeRealPolo - match1.getPopulacaoASerAtendida()) * 100.0 / capacidadeRealPolo, match1.getPercentualCapacidadePoloRestante());
            assertEquals(capacidadeRealPolo* 100.0 / comunidade.getPopulacao(), match1.getPercentualPopulacaoAtendidaEmRelacaoAoDeficit());
            assertEquals(MathUtils.calcularDistancia(comunidade.getLatitude(), comunidade.getLongitude(), poloFornecedor1.getLatitude(), poloFornecedor1.getLongitude()), match1.getDistancia());
            assertEquals(1, match1.getRankDistancia());

            MatchEnergeticoResponse match2 = matches.get(1);
            assertEquals(comunidade.getId(), match2.getComunidadeId());
            assertEquals(poloFornecedor2.getId(), match2.getPoloId());
            assertEquals(poloFornecedor2.getCapacidadeNormal(), match2.getPopulacaoASerAtendida());
            assertEquals(energia.getId(), match2.getEnergiaId());
            assertEquals(fornecedor.getId(), match2.getFornecedorId());
            assertEquals(comunidade.getPopulacao(), match2.getPopulacaoDeficitInicial());
            assertEquals(poloFornecedor2.getCapacidadeNormal() - match2.getPopulacaoASerAtendida(), match2.getCapacidadeMaximaPoloRestante());
            assertEquals((poloFornecedor2.getCapacidadeNormal() - match2.getPopulacaoASerAtendida()) * 100.0 / poloFornecedor2.getCapacidadeNormal(), match2.getPercentualCapacidadePoloRestante());
            assertEquals(poloFornecedor2.getCapacidadeNormal() * 100.0 / comunidade.getPopulacao(), match2.getPercentualPopulacaoAtendidaEmRelacaoAoDeficit());
            assertEquals(MathUtils.calcularDistancia(comunidade.getLatitude(), comunidade.getLongitude(), poloFornecedor2.getLatitude(), poloFornecedor2.getLongitude()), match2.getDistancia());
            assertEquals(2, match2.getRankDistancia());

            assertEquals(1, match1.getPopulacaoASerAtendida() > match2.getPopulacaoASerAtendida() ? match1.getRankSuprimentoDeficit() : match2.getRankSuprimentoDeficit());

            if (Objects.equals(match1.getPopulacaoASerAtendida(), match2.getPopulacaoASerAtendida())) {
                assertEquals(1, match1.getPopulacaoASerAtendida() < match2.getPopulacaoASerAtendida() ? match1.getRankSuprimentoDeficit() : match2.getRankSuprimentoDeficit());
            } else {
                assertEquals(2, match1.getPopulacaoASerAtendida() < match2.getPopulacaoASerAtendida() ? match1.getRankSuprimentoDeficit() : match2.getRankSuprimentoDeficit());
            }

            assertTrue(match1.getDistancia() < match2.getDistancia());
        });
    }

    @Test
    void testDeveLancarExceptionSeComunidadeNaoExistir() {
        assertThrows(RecursoNaoEncontradoException.class, () -> matchEnergeticoService.realizarMatchEnergetico(32000L));
    }

    @Test
    void testDeveLancarExceptionSeNenhumPoloForEncontrado() {
        // Criar Comunidade
        CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade A", "Localizacao A", "Descricao A", 20.0, 20.0, 3000L);
        AtomicReference<Comunidade> comunidade = new AtomicReference<>();

        Assertions.assertDoesNotThrow(() -> {
            comunidade.set(comunidadeService.createComunidade(createComunidadeDto));
        });

        assertNotNull(comunidade.get());
        assertThrows(RecursoNaoEncontradoException.class, () -> matchEnergeticoService.realizarMatchEnergetico(comunidade.get().getId()));
    }

    @Test
    void testDeveLancarExceptionSeNenhumPoloEstiverDisponivel() {
        Assertions.assertDoesNotThrow(() -> {
            // Criar Energia
            CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto("Energia Solar", "Energia obtida a partir do sol", null, "Renovável");
            Energia energia = energiaService.createEnergia(createEnergiaDto);
            assertNotNull(energia);

            // Criar Fornecedor
            CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto("Fornecedor A", "12345678901234", "Endereco A", "Descricao A");
            Fornecedor fornecedor = fornecedorService.createFornecedor(createFornecedorDto);
            assertNotNull(fornecedor);

            // Criar PoloFornecedor sem capacidade disponível
            CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto("Polo A", "Endereco A", 10.0, 10.0, fornecedor.getId(), energia.getId(), 2000L, 2000L);
            PoloFornecedor poloFornecedor = fornecedorService.createPoloFornecedor(createPoloFornecedorDto);
            assertNotNull(poloFornecedor);

            // Criar PoloFornecedor sem capacidade disponível 2
            CreatePoloFornecedorDto createPoloFornecedorDto2 = new CreatePoloFornecedorDto("Polo B", "Endereco B", 15.0, 15.0, fornecedor.getId(), energia.getId(), 0L, 0L);
            PoloFornecedor poloFornecedor2 = fornecedorService.createPoloFornecedor(createPoloFornecedorDto2);
            assertNotNull(poloFornecedor2);

            // Criar outra comunidade
            CreateComunidadeDto createComunidadeDto2 = new CreateComunidadeDto("Comunidade C", "Localizacao C", "Descricao C", 30.0, 30.0, poloFornecedor.getCapacidadeNormal() * 2);
            Comunidade comunidade2 = comunidadeService.createComunidade(createComunidadeDto2);

            assertNotNull(comunidade2);

            // Criar fornecimento energético para a comunidade 2
            CreateFornecimentoEnergeticoDto createFornecimentoEnergeticoDto = new CreateFornecimentoEnergeticoDto(comunidade2.getId(), poloFornecedor.getId(), poloFornecedor.getCapacidadeNormal());
            FornecimentoEnergetico fornecimentoEnergetico = fornecimentoEnergeticoService.createFornecimentoEnergetico(createFornecimentoEnergeticoDto);

            assertNotNull(fornecimentoEnergetico);

            // Criar Comunidade
            CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto("Comunidade A", "Localizacao A", "Descricao A", 20.0, 20.0, 3000L);
            Comunidade comunidade = comunidadeService.createComunidade(createComunidadeDto);
            assertNotNull(comunidade);

            // Tentar realizar Match
            assertThrows(NegocioException.class, () -> matchEnergeticoService.realizarMatchEnergetico(comunidade.getId()));
        });
    }
}