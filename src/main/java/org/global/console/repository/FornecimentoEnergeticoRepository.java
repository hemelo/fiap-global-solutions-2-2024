package org.global.console.repository;

import lombok.extern.slf4j.Slf4j;
import oracle.sql.ROWID;
import org.global.console.infra.DataSource;
import org.global.console.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe que representa o repositório de fornecimento energético.
 * Contém métodos para salvar, buscar, atualizar e deletar uso enegético.
 * É responsável por realizar a comunicação com o banco de dados.
 */
@Slf4j
public class FornecimentoEnergeticoRepository implements Repository<FornecimentoEnergetico> {

    private static FornecimentoEnergeticoRepository instance;
    private final DataSource dataSource;

    private FornecimentoEnergeticoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static synchronized FornecimentoEnergeticoRepository getInstance() {
        if (instance == null) {
            instance = new FornecimentoEnergeticoRepository(DataSource.getInstance());
        }

        return instance;
    }

    public FornecimentoEnergetico save(FornecimentoEnergetico fornecimentoEnergetico) throws SQLException {
        String query = "INSERT INTO fornecimento_energetico (polo_id, comunidade_id, populacao) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, fornecimentoEnergetico.getPoloId());
                stmt.setLong(2, fornecimentoEnergetico.getComunidadeId());
                stmt.setLong(3, fornecimentoEnergetico.getPopulacao());
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();

                if (rs.next()) {
                    try {
                        fornecimentoEnergetico.setId(rs.getLong(1));
                    } catch (Exception e) {
                        try {

                            ROWID rowid = (ROWID) rs.getObject(1);
                            String query2 = "SELECT id FROM fornecimento_energetico WHERE ROWID = ?";

                            try (PreparedStatement stmt2 = connection.prepareStatement(query2)) {
                                stmt2.setString(1, rowid.stringValue());
                                rs = stmt2.executeQuery();
                                rs.next();
                                fornecimentoEnergetico.setId(rs.getLong(1));
                            }

                        } catch (Exception ex) {
                            log.error("Erro ao buscar ID do fornecimento energético", ex);
                        }
                    }
                }

            } catch (Exception e) {
                log.error("Erro ao salvar fornecimento energético", e);
                throw e;
            }
        }

        return fornecimentoEnergetico;
    }

    public FornecimentoEnergetico findById(Long id) throws SQLException {
        String query = "SELECT * FROM fornecimento_energetico WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return mapResultToFornecimentoEnergetico(rs, false);
                }

                log.info("fornecimento energético {} não encontrado", id);
            } catch (Exception e) {
                log.error("Erro ao buscar fornecimento energético", e);
                throw e;
            }
        }

        return null;
    }

    public List<FornecimentoEnergetico> findAllByComunidadeId(Long comunidadeId) throws SQLException {
        List<FornecimentoEnergetico> fornecimentoEnergeticos = new ArrayList<>();
        String query = "SELECT * FROM fornecimento_energetico WHERE comunidade_id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, comunidadeId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    fornecimentoEnergeticos.add(mapResultToFornecimentoEnergetico(rs, false));
                }
            } catch (Exception e) {
                log.error("Erro ao buscar fornecimento energético", e);
                throw e;
            }
        }

        return fornecimentoEnergeticos;
    }

    public FornecimentoEnergetico findByIdDetailed(Long id) throws SQLException {
        String query = getDetailedQuerySelect() + " WHERE fe.id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return mapResultToFornecimentoEnergetico(rs, true);
                }

                log.info("fornecimento energético {} não encontrado", id);
            } catch (Exception e) {
                log.error("Erro ao buscar fornecimento energético", e);
                throw e;
            }
        }

        return null;
    }

    public List<FornecimentoEnergetico> findAllDetailedByComunidadeId(Long comunidadeId) throws SQLException {

        List<FornecimentoEnergetico> fornecimentoEnergeticos = new ArrayList<>();

        String query = getDetailedQuerySelect() + " WHERE fe.comunidade_id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, comunidadeId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    fornecimentoEnergeticos.add(mapResultToFornecimentoEnergetico(rs, true));
                }

            } catch (Exception e) {
                log.error("Erro ao buscar fornecimento energético", e);
                throw e;
            }
        }

        return fornecimentoEnergeticos;
    }

    public List<FornecimentoEnergetico> findAll() throws SQLException {
        List<FornecimentoEnergetico> fornecimentoEnergeticos = new ArrayList<>();
        String query = "SELECT * FROM fornecimento_energetico";

        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    fornecimentoEnergeticos.add(mapResultToFornecimentoEnergetico(rs, false));
                }
            } catch (Exception e) {
                log.error("Erro ao buscar fornecimento energético", e);
                throw e;
            }
        }

        return fornecimentoEnergeticos;
    }

    public List<FornecimentoEnergetico> findAllDetailed() throws SQLException {
        List<FornecimentoEnergetico> fornecimentoEnergeticos = new ArrayList<>();
        String query = getDetailedQuerySelect();

        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    fornecimentoEnergeticos.add(mapResultToFornecimentoEnergetico(rs, true));
                }
            } catch (Exception e) {
                log.error("Erro ao buscar fornecimento energético", e);
                throw e;
            }
        }

        return fornecimentoEnergeticos;
    }

    public FornecimentoEnergetico update(FornecimentoEnergetico fornecimentoEnergetico) throws SQLException {
        String query = "UPDATE fornecimento_energetico SET populacao = ?, polo_id = ?, comunidade_id = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, fornecimentoEnergetico.getPopulacao());
                stmt.setLong(2, fornecimentoEnergetico.getPoloId());
                stmt.setLong(3, fornecimentoEnergetico.getComunidadeId());
                stmt.setLong(4, fornecimentoEnergetico.getId());
                stmt.executeUpdate();

                log.info("fornecimento energético {} atualizado com sucesso", fornecimentoEnergetico.getId());
            } catch (Exception e) {
                log.error("Erro ao atualizar fornecimento energético", e);
                throw e;
            }
        }

        return fornecimentoEnergetico;
    }

    public boolean delete(Long id) throws SQLException {
        String query = "DELETE FROM fornecimento_energetico WHERE id = ?";
        Boolean result;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                result = stmt.executeUpdate() > 0;
                log.info(result ? "fornecimento energético {} deletado com sucesso" : "fornecimento energético {} não encontrado para deleção", id);
                return result;
            } catch (Exception e) {
                log.error("Erro ao deletar fornecimento energético", e);
                throw e;
            }
        }
    }

    private String getDetailedQuerySelect() {
        return """
                SELECT
                    fe.id AS fornecimento_id,
                    fe.populacao,
                    fe.created_at,
                    fe.updated_at,
                    p.id AS polo_id,
                    f.id AS fornecedor_id,
                    e.id AS energia_id,
                    c.id AS comunidade_id,
                    p.nome AS polo_nome,
                    p.capacidade_populacional as polo_capacidade_populacional,
                    p.capacidade_populacional_maxima as polo_capacidade_populacional_maxima,
                    p.endereco as polo_endereco,
                    p.latitude as polo_latitude,
                    p.longitude as polo_longitude,
                    f.nome AS fornecedor_nome,
                    f.cnpj AS fornecedor_cnpj,
                    f.endereco AS fornecedor_endereco,
                    f.descricao AS fornecedor_descricao,
                    c.nome AS comunidade_nome,
                    c.localizacao AS comunidade_localizacao,
                    c.descricao AS comunidade_descricao,
                    c.latitude AS comunidade_latitude,
                    c.longitude AS comunidade_longitude,
                    c.populacao AS comunidade_populacao,
                    e.tipo AS energia_tipo,
                    e.nome AS energia_nome,
                    e.descricao AS energia_descricao
                FROM
                    fornecimento_energetico fe
                JOIN
                    polo_fornecedor p ON fe.polo_id = p.id
                JOIN
                    fornecedor f ON p.id_fornecedor = f.id
                JOIN
                    energia e ON p.id_energia = e.id
                JOIN
                    comunidade c ON fe.comunidade_id = c.id
                """;
    }

    private FornecimentoEnergetico mapResultToFornecimentoEnergetico(ResultSet resultSet, Boolean detailed) throws SQLException {

        Comunidade comunidade = null;
        PoloFornecedor poloFornecedor = null;
        Fornecedor fornecedor = null;
        Energia energia = null;

        if (detailed) {
            try {
                comunidade = Comunidade.builder()
                        .id(resultSet.getLong("comunidade_id"))
                        .nome(resultSet.getString("comunidade_nome"))
                        .localizacao(resultSet.getString("comunidade_localizacao"))
                        .descricao(resultSet.getString("comunidade_descricao"))
                        .latitude(resultSet.getDouble("comunidade_latitude"))
                        .longitude(resultSet.getDouble("comunidade_longitude"))
                        .populacao(resultSet.getLong("comunidade_populacao"))
                        .build();
            } catch (SQLException ex) {
                log.error("Erro ao mapear comunidade", ex);
            }

            try {
                fornecedor = Fornecedor.builder()
                        .id(resultSet.getLong("fornecedor_id"))
                        .nome(resultSet.getString("fornecedor_nome"))
                        .cnpj(resultSet.getString("fornecedor_cnpj"))
                        .endereco(resultSet.getString("fornecedor_endereco"))
                        .descricao(resultSet.getString("fornecedor_descricao"))
                        .build();
            } catch (SQLException ex) {
                log.error("Erro ao mapear fornecedor", ex);
            }

            try {
                energia = Energia.builder()
                        .id(resultSet.getLong("energia_id"))
                        .tipo(resultSet.getString("energia_tipo"))
                        .nome(resultSet.getString("energia_nome"))
                        .descricao(resultSet.getString("energia_descricao"))
                        .build();
            } catch (SQLException ex) {
                log.error("Erro ao mapear energia", ex);
            }

            try {
                poloFornecedor = PoloFornecedor.builder()
                        .id(resultSet.getLong("polo_id"))
                        .nome(resultSet.getString("polo_nome"))
                        .capacidadeNormal(resultSet.getLong("polo_capacidade_populacional"))
                        .capacidadeMaxima(resultSet.getLong("polo_capacidade_populacional_maxima"))
                        .endereco(resultSet.getString("polo_endereco"))
                        .latitude(resultSet.getDouble("polo_latitude"))
                        .longitude(resultSet.getDouble("polo_longitude"))
                        .fornecedorId(resultSet.getLong("id_fornecedor"))
                        .fornecedor(fornecedor)
                        .energiaId(resultSet.getLong("energia_id"))
                        .energia(energia)
                        .build();

            } catch (SQLException ex) {
                log.error("Erro ao mapear polo", ex);
            }
        }


        return FornecimentoEnergetico.builder()
                .id(resultSet.getLong(1))
                .poloId(resultSet.getLong("polo_id"))
                .comunidadeId(resultSet.getLong("comunidade_id"))
                .poloFornecedor(poloFornecedor)
                .comunidade(comunidade)
                .populacao(resultSet.getLong("populacao"))
                .createdAt(Optional.ofNullable(resultSet.getTimestamp("created_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .updatedAt(Optional.ofNullable(resultSet.getTimestamp("updated_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .build();
    }


    @Override
    public void truncate() {
        String query = "TRUNCATE TABLE fornecimento_energetico";

        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
                log.info("Tabela fornecimento_energetico truncada com sucesso");
            } catch (Exception e) {
                log.error("Erro ao truncar tabela fornecimento_energetico", e);
            }
        } catch (SQLException e) {
            log.error("Erro ao conectar ao banco de dados", e);
        }
    }
}
