package org.global.console.repository;

import lombok.extern.slf4j.Slf4j;
import oracle.sql.ROWID;
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
public class FornecedorRepository implements Repository<Fornecedor> {

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
        String query = "INSERT INTO fornecedor (nome, cnpj, descricao, endereco) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, fornecedor.getNome());
                stmt.setString(2, fornecedor.getCnpj());
                stmt.setString(3, fornecedor.getDescricao());
                stmt.setString(4, fornecedor.getEndereco());
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();

                if (rs.next()) {
                    try {
                        fornecedor.setId(rs.getLong(1));
                    } catch (Exception e) {
                        try {

                            ROWID rowid = (ROWID) rs.getObject(1);
                            String query2 = "SELECT id FROM fornecedor WHERE ROWID = ?";

                            try (PreparedStatement stmt2 = connection.prepareStatement(query2)) {
                                stmt2.setString(1, rowid.stringValue());
                                rs = stmt2.executeQuery();
                                rs.next();
                                fornecedor.setId(rs.getLong(1));
                            }

                        } catch (Exception ex) {
                            log.error("Erro ao buscar id do fornecedor", ex);
                        }
                    }
                }

                log.info("Fornecedor {} salvo com sucesso", fornecedor.getId());

            } catch (Exception e) {
                log.error("Erro ao salvar fornecedor", e);
                throw e;
            }
        }

        return fornecedor;
    }

    public Fornecedor findById(Long id) throws SQLException {
        String query = "SELECT * FROM fornecedor WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return mapResultSetToFornecedor(rs);
                }

                log.info("Fornecedor com id {} não encontrado", id);
            } catch (Exception e) {
                log.error("Erro ao buscar fornecedor", e);
                throw e;
            }
        }

        return null;
    }

    public void update(Fornecedor fornecedor) throws SQLException {
        String query = "UPDATE fornecedor SET nome = ?, cnpj = ?, descricao = ?, endereco = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, fornecedor.getNome());
                stmt.setString(2, fornecedor.getCnpj());
                stmt.setString(3, fornecedor.getDescricao());
                stmt.setString(4, fornecedor.getEndereco());
                stmt.setLong(5, fornecedor.getId());
                stmt.executeUpdate();

                log.info("Fornecedor {} atualizado com sucesso", fornecedor.getId());
            } catch (Exception e) {
                log.error("Erro ao atualizar fornecedor", e);
                throw e;
            }
        }
    }

    public boolean delete(Long id) throws SQLException {
        String query = "DELETE FROM fornecedor WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                Boolean result = stmt.executeUpdate() > 0;
                log.info(result ? "Fornecedor {} deletado com sucesso" : "Fornecedor {} não encontrado para deleção", id);
                return result;
            } catch (Exception e) {
                log.error("Erro ao deletar fornecedor", e);
                throw e;
            }
        }
    }

    public List<Fornecedor> findAll() throws SQLException {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String query = "SELECT * FROM fornecedor";

        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    fornecedores.add(mapResultSetToFornecedor(rs));
                }
            } catch (Exception e) {
                log.error("Erro ao buscar fornecedores", e);
                throw e;
            }
        }

        return fornecedores;
    }

    private Fornecedor mapResultSetToFornecedor(ResultSet rs) throws SQLException {
        return Fornecedor.builder()
                .id(rs.getLong("id"))
                .nome(rs.getString("nome"))
                .cnpj(rs.getString("cnpj"))
                .descricao(rs.getString("descricao"))
                .endereco(rs.getString("endereco"))
                .createdAt(Optional.ofNullable(rs.getTimestamp("created_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .updatedAt(Optional.ofNullable(rs.getTimestamp("updated_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .build();
    }

    @Override
    public void truncate() {
        String query = "TRUNCATE TABLE fornecedor";

        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
                log.info("Tabela fornecedor truncada com sucesso");
            } catch (Exception e) {
                log.error("Erro ao truncar tabela fornecedor", e);
            }
        } catch (SQLException e) {
            log.error("Erro ao conectar ao banco de dados", e);
        }
    }
}