package org.global.console.repository;

import lombok.extern.slf4j.Slf4j;
import org.global.console.enums.UnidadeMedida;
import org.global.console.infra.DataSource;
import org.global.console.model.Energia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe que representa o repositório de energias.
 * Contém métodos para salvar, buscar, atualizar e deletar energias.
 * É responsável por realizar a comunicação com o banco de dados.
 */
@Slf4j
public class EnergiaRepository {

    private static EnergiaRepository instance;
    private final DataSource dataSource;

    private EnergiaRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static synchronized EnergiaRepository getInstance() {
        if (instance == null) {
            instance = new EnergiaRepository(DataSource.getInstance());
        }

        return instance;
    }

    public Energia save(Energia energia) throws SQLException {
        String query = "INSERT INTO Energia (tipo, nome, descricao) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, energia.getTipo());
            stmt.setString(2, energia.getNome());
            stmt.setString(3, energia.getDescricao());
            //stmt.setString(4, energia.getUnidade().name());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                try {
                    energia.setId(rs.getLong(1));
                } catch (SQLException e) {
                    try {
                        energia.setId(Long.valueOf(rs.getString(1)));
                    } catch (SQLException ex) {
                        log.error("Erro ao buscar id da fonte de energia", ex);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erro ao salvar energia", e);
            throw e;
        }

        return energia;
    }

    public Energia findById(Long id) throws SQLException {
        String query = "SELECT * FROM energia WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEnergia(rs);
            }
        } catch (Exception e) {
            log.error("Erro ao buscar energia", e);
            throw e;
        }

        return null;
    }

    public List<Energia> findAll() throws SQLException {
        List<Energia> energias = new ArrayList<>();
        String query = "SELECT * FROM energia";

        try (Statement stmt = dataSource.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                energias.add(mapResultSetToEnergia(rs));
            }
        } catch (Exception e) {
            log.error("Erro ao buscar todas as energias", e);
            throw e;
        }

        return energias;
    }

    public Energia update(Energia energia) throws SQLException {
        String query = "UPDATE energia SET tipo = ?, nome = ?, descricao = ? WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, energia.getTipo());
            stmt.setString(2, energia.getNome());
            stmt.setString(3, energia.getDescricao());
            //stmt.setString(2, energia.getUnidade().name());
            stmt.setLong(4, energia.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            log.error("Erro ao atualizar energia", e);
            throw e;
        }

        return energia;
    }

    public boolean delete(Long id) throws SQLException {
        String query = "DELETE FROM energia WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            log.error("Erro ao deletar energia", e);
            throw e;
        }
    }

    private Energia mapResultSetToEnergia(ResultSet resultSet) throws SQLException {
        return Energia.builder()
                .id(resultSet.getLong("id"))
                .tipo(resultSet.getString("tipo"))
                //.unidade(UnidadeMedida.valueOf(resultSet.getString("unidade")))
                .nome(resultSet.getString("nome"))
                .descricao(resultSet.getString("descricao"))
                .createdAt(Optional.ofNullable(resultSet.getTimestamp("created_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .updatedAt(Optional.ofNullable(resultSet.getTimestamp("updated_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .build();
    }
}
