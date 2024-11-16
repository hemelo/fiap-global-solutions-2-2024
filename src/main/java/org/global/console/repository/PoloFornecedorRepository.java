package org.global.console.repository;

import lombok.extern.slf4j.Slf4j;
import org.global.console.infra.DataSource;
import org.global.console.model.Energia;
import org.global.console.model.Fornecedor;
import org.global.console.model.PoloFornecedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PoloFornecedorRepository {

    private static PoloFornecedorRepository instance;
    private final DataSource dataSource;

    private PoloFornecedorRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static synchronized PoloFornecedorRepository getInstance() {
        if (instance == null) {
            instance = new PoloFornecedorRepository(DataSource.getInstance());
        }

        return instance;
    }

    public PoloFornecedor save(PoloFornecedor poloFornecedor) throws SQLException {
        String query = "INSERT INTO polo_fornecedor (nome, endereco, fornecedor_id, latitude, longitude) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, poloFornecedor.getNome());
            stmt.setString(2, poloFornecedor.getEndereco());
            stmt.setLong(3, poloFornecedor.getFornecedorId());
            stmt.setDouble(4, poloFornecedor.getLatitude());
            stmt.setDouble(5, poloFornecedor.getLongitude());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                try {
                    poloFornecedor.setId(rs.getLong(1));
                } catch (SQLException e) {
                    try {
                        poloFornecedor.setId(Long.valueOf(rs.getString(1)));
                    } catch (SQLException ex) {
                        log.error("Erro ao buscar id do polo fornecedor", ex);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erro ao salvar polo fornecedor", e);
            throw e;
        }

        return poloFornecedor;
    }

    public PoloFornecedor findById(Long id) throws SQLException {
        String query = "SELECT * FROM polo_fornecedor WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToFornecedor(rs);
            }
        } catch (Exception e) {
            log.error("Erro ao buscar polo fornecedor por ID", e);
            throw e;
        }

        return null;
    }

    public void update(PoloFornecedor poloFornecedor) throws SQLException {
        String query = "UPDATE polo_fornecedor SET nome = ?, endereco = ?, fornecedor_id = ?, latitude = ?, longitude = ? WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, poloFornecedor.getNome());
            stmt.setString(2, poloFornecedor.getEndereco());
            stmt.setLong(3, poloFornecedor.getFornecedorId());
            stmt.setDouble(4, poloFornecedor.getLatitude());
            stmt.setDouble(5, poloFornecedor.getLongitude());
            stmt.setLong(6, poloFornecedor.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            log.error("Erro ao atualizar polo fornecedor", e);
            throw e;
        }
    }

    public List<PoloFornecedor> findByFornecedorId(Long fornecedorId) throws SQLException {
        List<PoloFornecedor> polos = new ArrayList<>();
        String query = "SELECT * FROM polo_fornecedor WHERE fornecedor_id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, fornecedorId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                polos.add(mapResultSetToFornecedor(rs));
            }
        } catch (Exception e) {
            log.error("Erro ao buscar polo fornecedor por ID do fornecedor", e);
            throw e;
        }

        return polos;
    }

    public List<PoloFornecedor> findAll() throws SQLException {
        List<PoloFornecedor> polos = new ArrayList<>();
        String query = "SELECT * FROM polo_fornecedor";

        try (Statement stmt = dataSource.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                polos.add(mapResultSetToFornecedor(rs));
            }
        } catch (Exception e) {
            log.error("Erro ao buscar polos fornecedores", e);
            throw e;
        }

        return polos;
    }

    public void delete(Long id) throws SQLException {
        String query = "DELETE FROM polo_fornecedor WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            log.error("Erro ao deletar polo fornecedor", e);
            throw e;
        }
    }

    private PoloFornecedor mapResultSetToFornecedor(ResultSet rs) throws SQLException {
        return PoloFornecedor.builder()
                .id(rs.getLong("id"))
                .nome(rs.getString("nome"))
                .endereco(rs.getString("endereco"))
                .fornecedorId(rs.getLong("fornecedor_id"))
                .latitude(rs.getDouble("latitude"))
                .longitude(rs.getDouble("longitude"))
                .createdAt(Optional.ofNullable(rs.getTimestamp("created_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .updatedAt(Optional.ofNullable(rs.getTimestamp("updated_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .build();
    }
}
