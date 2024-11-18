package org.global.console.repository;

import lombok.extern.slf4j.Slf4j;
import oracle.sql.ROWID;
import org.global.console.infra.DataSource;
import org.global.console.model.Comunidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe que representa o repositório de comunidades.
 * Contém métodos para salvar, buscar, atualizar e deletar comunidades.
 * É responsável por realizar a comunicação com o banco de dados.
 */
@Slf4j
public class ComunidadeRepository implements Repository<Comunidade> {

    private static ComunidadeRepository instance;
    private final DataSource dataSource;

    private ComunidadeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static synchronized ComunidadeRepository getInstance() {
        if (instance == null) {
            instance = new ComunidadeRepository(DataSource.getInstance());
        }

        return instance;
    }

    public Comunidade save(Comunidade comunidade) throws SQLException {
        String query = "INSERT INTO Comunidade (nome, localizacao, descricao, latitude, longitude, populacao) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, comunidade.getNome());
                stmt.setString(2, comunidade.getLocalizacao());
                stmt.setString(3, comunidade.getDescricao());
                stmt.setDouble(4, comunidade.getLatitude());
                stmt.setDouble(5, comunidade.getLongitude());
                stmt.setLong(6, comunidade.getPopulacao());

                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();

                if (rs.next()) {
                    try {
                        comunidade.setId(rs.getLong(1));
                    } catch (Exception e) {
                        try {
                            ROWID rowid = (ROWID) rs.getObject(1);
                            String query2 = "SELECT id FROM comunidade WHERE ROWID = ?";

                            try (PreparedStatement stmt2 = connection.prepareStatement(query2)) {
                                stmt2.setString(1, rowid.stringValue());
                                rs = stmt2.executeQuery();
                                rs.next();
                                comunidade.setId(rs.getLong(1));
                            }

                        } catch (Exception ex) {
                            log.error("Erro ao buscar id da comunidade", ex);
                        }
                    }
                }

                log.info("Comunidade {} salva com sucesso", comunidade.getId());

            } catch (Exception e) {
                log.error("Erro ao salvar comunidade", e);
                throw e;
            }
        }

        return comunidade;
    }

    public Comunidade findById(Long id) throws SQLException {
        String query = "SELECT * FROM comunidade WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return mapResultSetToComunidade(rs);
                }

                log.info("Comunidade {} não encontrada", id);
            } catch (Exception e) {
                log.error("Erro ao buscar comunidade por ID", e);
                throw e;
            }
        }

        return null;
    }

    public List<Comunidade> findAll() throws SQLException {
        List<Comunidade> comunidades = new ArrayList<>();
        String query = "SELECT * FROM comunidade";

        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    comunidades.add(mapResultSetToComunidade(rs));
                }

            } catch (Exception e) {
                log.error("Erro ao buscar comunidades", e);
                throw e;
            }
        }

        return comunidades;
    }

    public Comunidade update(Comunidade comunidade) throws SQLException {
        String query = "UPDATE comunidade SET nome = ?, localizacao = ?, descricao = ?, latitude = ?, longitude = ?, populacao = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, comunidade.getNome());
                stmt.setString(2, comunidade.getLocalizacao());
                stmt.setString(3, comunidade.getDescricao());
                stmt.setLong(4, comunidade.getId());
                stmt.setDouble(5, comunidade.getLatitude());
                stmt.setDouble(6, comunidade.getLongitude());
                stmt.setLong(7, comunidade.getPopulacao());
                stmt.executeUpdate();

                log.info("Comunidade {} atualizada com sucesso", comunidade.getId());
            } catch (Exception e) {
                log.error("Erro ao atualizar comunidade", e);
                throw e;
            }
        }

        return comunidade;
    }

    public boolean delete(Long id) throws SQLException {
        String query = "DELETE FROM comunidade WHERE id = ?";
        Boolean result;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                result = stmt.executeUpdate() > 0;
                log.info((result ? "Comunidade {} deletada com sucesso" : "Comunidade {} não encontrada para deleção"), id);
                return result;
            } catch (Exception e) {
                log.error("Erro ao deletar comunidade", e);
                throw e;
            }
        }
    }

    private Comunidade mapResultSetToComunidade(ResultSet resultSet) throws SQLException {
        return Comunidade.builder()
                .id(resultSet.getLong("id"))
                .populacao(resultSet.getLong("populacao"))
                .nome(resultSet.getString("nome"))
                .localizacao(resultSet.getString("localizacao"))
                .descricao(resultSet.getString("descricao"))
                .latitude(resultSet.getDouble("latitude"))
                .longitude(resultSet.getDouble("longitude"))
                .createdAt(Optional.ofNullable(resultSet.getTimestamp("created_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .updatedAt(Optional.ofNullable(resultSet.getTimestamp("updated_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .build();
    }

    @Override
    public void truncate() {
        String query = "TRUNCATE TABLE comunidade";

        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
                log.info("Tabela comunidade truncada com sucesso");
            } catch (Exception e) {
                log.error("Erro ao truncar tabela comunidade", e);
            }
        } catch (SQLException e) {
            log.error("Erro ao conectar ao banco de dados", e);
        }
    }
}
