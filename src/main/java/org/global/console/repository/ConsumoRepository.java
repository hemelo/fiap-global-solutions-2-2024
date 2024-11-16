package org.global.console.repository;

import lombok.extern.slf4j.Slf4j;
import org.global.console.enums.UnidadeMedida;
import org.global.console.infra.DataSource;
import org.global.console.model.Consumo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe que representa o repositório de consumos.
 * Contém métodos para salvar, buscar, atualizar e deletar consumos.
 * É responsável por realizar a comunicação com o banco de dados.
 */
@Slf4j
public class ConsumoRepository {

    private static ConsumoRepository instance;
    private final DataSource dataSource;

    private ConsumoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static synchronized ConsumoRepository getInstance() {
        if (instance == null) {
            instance = new ConsumoRepository(DataSource.getInstance());
        }

        return instance;
    }

    public Consumo save(Consumo consumo) throws SQLException {
        String query = "INSERT INTO consumo (quantidade, fornecedor_id, energia_id, comunidade_id, unidade) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, consumo.getQuantidade());
            stmt.setLong(2, consumo.getFornecedorId());
            stmt.setLong(3, consumo.getEnergiaId());
            stmt.setLong(4, consumo.getComunidadeId());
            stmt.setString(5, consumo.getUnidade().name());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                try {
                    consumo.setId(rs.getLong(1));
                } catch (SQLException e) {
                    try {
                        consumo.setId(Long.valueOf(rs.getString(1)));
                    } catch (SQLException ex) {
                        log.error("Erro ao buscar ID do consumo", ex);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erro ao salvar consumo", e);
            throw e;
        }

        return consumo;
    }

    public Consumo findById(Long id) throws SQLException {
        String query = "SELECT * FROM consumo WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToConsumo(rs);
            }
        } catch (Exception e) {
            log.error("Erro ao buscar consumo", e);
            throw e;
        }

        return null;
    }

    public List<Consumo> findAll() throws SQLException {
        List<Consumo> consumos = new ArrayList<>();
        String query = "SELECT * FROM consumo";

        try (Statement stmt = dataSource.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                consumos.add(mapResultSetToConsumo(rs));
            }
        } catch (Exception e) {
            log.error("Erro ao buscar consumos", e);
            throw e;
        }

        return consumos;
    }

    public Consumo update(Consumo consumo) throws SQLException {
        String query = "UPDATE consumo SET quantidade = ?, unidade = ?, fornecedor_id = ?, energia_id = ?, comunidade_id = ? WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setDouble(1, consumo.getQuantidade());
            stmt.setString(2, consumo.getUnidade().name());
            stmt.setLong(3, consumo.getFornecedorId());
            stmt.setLong(4, consumo.getEnergiaId());
            stmt.setLong(5, consumo.getComunidadeId());
            stmt.setLong(6, consumo.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            log.error("Erro ao atualizar consumo", e);
            throw e;
        }

        return consumo;
    }

    public boolean delete(Long id) throws SQLException {
        String query = "DELETE FROM consumo WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            log.error("Erro ao deletar consumo", e);
            throw e;
        }
    }

    private Consumo mapResultSetToConsumo(ResultSet resultSet) throws SQLException {
        return Consumo.builder()
                .id(resultSet.getLong("id"))
                .energiaId(resultSet.getLong("energia_id"))
                .comunidadeId(resultSet.getLong("comunidade_id"))
                .fornecedorId(resultSet.getLong("fornecedor_id"))
                .unidade(UnidadeMedida.valueOf(resultSet.getString("unidade")))
                .quantidade(resultSet.getDouble("quantidade"))
                .createdAt(Optional.ofNullable(resultSet.getTimestamp("created_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .updatedAt(Optional.ofNullable(resultSet.getTimestamp("updated_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .build();
    }
}
