package org.global.console.repository;

import lombok.extern.slf4j.Slf4j;
import org.global.console.infra.DataSource;
import org.global.console.model.Fornecedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe que representa o repositório de fornecedores.
 * Contém métodos para salvar, buscar, atualizar e deletar fornecedores.
 * É responsável por realizar a comunicação com o banco de dados.
 */
@Slf4j
public class FornecedorRepository {

    private static FornecedorRepository instance;
    private final DataSource dataSource;

    private FornecedorRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static synchronized FornecedorRepository getInstance() {
        if (instance == null) {
            instance = new FornecedorRepository(DataSource.getInstance());
        }

        return instance;
    }

    public Fornecedor save(Fornecedor fornecedor) throws SQLException {
        String query = "INSERT INTO fornecedor (nome, cnpj, descricao) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getDescricao());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                try {
                    fornecedor.setId(rs.getLong(1));
                } catch (SQLException e) {
                    try {
                        fornecedor.setId(Long.valueOf(rs.getString(1)));
                    } catch (SQLException ex) {
                        log.error("Erro ao buscar id do fornecedor", ex);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erro ao salvar fornecedor", e);
            throw e;
        }

        return fornecedor;
    }

    public Fornecedor findById(Long id) throws SQLException {
        String query = "SELECT * FROM fornecedor WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToFornecedor(rs);
            }
        } catch (Exception e) {
            log.error("Erro ao buscar fornecedor", e);
            throw e;
        }

        return null;
    }

    public void update(Fornecedor fornecedor) throws SQLException {
        String query = "UPDATE fornecedor SET nome = ?, cnpj = ?, descricao = ? WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getDescricao());
            stmt.setLong(4, fornecedor.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            log.error("Erro ao atualizar fornecedor", e);
            throw e;
        }
    }

    public void delete(Long id) throws SQLException {
        String query = "DELETE FROM fornecedor WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            log.error("Erro ao deletar fornecedor", e);
            throw e;
        }
    }

    public List<Fornecedor> findAll() throws SQLException {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String query = "SELECT * FROM fornecedor";

        try (Statement stmt = dataSource.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                fornecedores.add(mapResultSetToFornecedor(rs));
            }
        } catch (Exception e) {
            log.error("Erro ao buscar fornecedores", e);
            throw e;
        }

        return fornecedores;
    }

    private Fornecedor mapResultSetToFornecedor(ResultSet rs) throws SQLException {
        return Fornecedor.builder()
                .id(rs.getLong("id"))
                .nome(rs.getString("nome"))
                .cnpj(rs.getString("cnpj"))
                .descricao(rs.getString("descricao"))
                .createdAt(Optional.ofNullable(rs.getTimestamp("created_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .updatedAt(Optional.ofNullable(rs.getTimestamp("updated_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .build();
    }
}