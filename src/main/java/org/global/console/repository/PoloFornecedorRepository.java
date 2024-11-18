package org.global.console.repository;

import lombok.extern.slf4j.Slf4j;
import oracle.sql.ROWID;
import org.global.console.infra.DataSource;
import org.global.console.model.Energia;
import org.global.console.model.Fornecedor;
import org.global.console.model.PoloFornecedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PoloFornecedorRepository implements Repository<PoloFornecedor> {

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
        String query = "INSERT INTO polo_fornecedor (nome, endereco, id_fornecedor, latitude, longitude, capacidade_populacional, capacidade_populacional_maxima, id_energia) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, poloFornecedor.getNome());
                stmt.setString(2, poloFornecedor.getEndereco());
                stmt.setLong(3, poloFornecedor.getFornecedorId());
                stmt.setDouble(4, poloFornecedor.getLatitude());
                stmt.setDouble(5, poloFornecedor.getLongitude());
                stmt.setLong(6, poloFornecedor.getCapacidadeNormal());
                stmt.setLong(7, poloFornecedor.getCapacidadeMaxima());
                stmt.setLong(8, poloFornecedor.getEnergiaId());
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();

                if (rs.next()) {
                    try {

                        ROWID rowid = (ROWID) rs.getObject(1);
                        String query2 = "SELECT id FROM polo_fornecedor WHERE ROWID = ?";

                        try (PreparedStatement stmt2 = connection.prepareStatement(query2)) {
                            stmt2.setString(1, rowid.stringValue());
                            rs = stmt2.executeQuery();
                            rs.next();
                            poloFornecedor.setId(rs.getLong(1));
                        }

                    } catch (Exception e) {
                        try {
                            poloFornecedor.setId(Long.valueOf(rs.getString(1)));
                        } catch (Exception ex) {
                            log.error("Erro ao buscar id do polo fornecedor", ex);
                        }
                    }
                }

                log.info("Polo fornecedor {} salvo com sucesso", poloFornecedor.getId());

            } catch (Exception e) {
                log.error("Erro ao salvar polo fornecedor", e);
                throw e;
            }
        }

        return poloFornecedor;
    }

    public PoloFornecedor findById(Long id) throws SQLException {
        String query = "SELECT * FROM polo_fornecedor WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return mapResultSetToFornecedor(rs);
                }

                log.info("Polo fornecedor com ID {} não encontrado", id);
            } catch (Exception e) {
                log.error("Erro ao buscar polo fornecedor por ID", e);
                throw e;
            }
        }

        return null;
    }

    public void update(PoloFornecedor poloFornecedor) throws SQLException {
        String query = "UPDATE polo_fornecedor SET nome = ?, endereco = ?, id_fornecedor = ?, latitude = ?, longitude = ?, capacidade_populacional = ?, capacidade_populacional_maxima = ?, id_energia = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, poloFornecedor.getNome());
                stmt.setString(2, poloFornecedor.getEndereco());
                stmt.setLong(3, poloFornecedor.getFornecedorId());
                stmt.setDouble(4, poloFornecedor.getLatitude());
                stmt.setDouble(5, poloFornecedor.getLongitude());
                stmt.setLong(6, poloFornecedor.getCapacidadeNormal());
                stmt.setLong(7, poloFornecedor.getCapacidadeMaxima());
                stmt.setLong(8, poloFornecedor.getEnergiaId());
                stmt.setLong(9, poloFornecedor.getId());
                stmt.executeUpdate();

                log.info("Polo fornecedor {} atualizado com sucesso", poloFornecedor.getId());
            } catch (Exception e) {
                log.error("Erro ao atualizar polo fornecedor", e);
                throw e;
            }
        }
    }

    public List<PoloFornecedor> findByFornecedorId(Long fornecedorId) throws SQLException {
        List<PoloFornecedor> polos = new ArrayList<>();
        String query = "SELECT * FROM polo_fornecedor WHERE id_fornecedor = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, fornecedorId);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    polos.add(mapResultSetToFornecedor(rs));
                }
            } catch (Exception e) {
                log.error("Erro ao buscar polo fornecedor por ID do fornecedor", e);
                throw e;
            }
        }

        return polos;
    }

    public List<PoloFornecedor> findAll() throws SQLException {
        List<PoloFornecedor> polos = new ArrayList<>();
        String query = "SELECT * FROM polo_fornecedor";

        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    polos.add(mapResultSetToFornecedor(rs));
                }
            } catch (Exception e) {
                log.error("Erro ao buscar polos fornecedores", e);
                throw e;
            }
        }

        return polos;
    }

    public boolean delete(Long id) throws SQLException {
        String query = "DELETE FROM polo_fornecedor WHERE id = ?";
        Boolean result;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                result = stmt.executeUpdate() > 0;
                log.info(result ? "Polo fornecedor {} deletado com sucesso" : "Polo fornecedor {} não encontrado para deleção", id);
                return result;
            } catch (Exception e) {
                log.error("Erro ao deletar polo fornecedor", e);
                throw e;
            }
        }
    }

    private PoloFornecedor mapResultSetToFornecedor(ResultSet rs) throws SQLException {
        return PoloFornecedor.builder()
                .id(rs.getLong("id"))
                .nome(rs.getString("nome"))
                .endereco(rs.getString("endereco"))
                .fornecedorId(rs.getLong("id_fornecedor"))
                .energiaId(rs.getLong("id_energia"))
                .latitude(rs.getDouble("latitude"))
                .longitude(rs.getDouble("longitude"))
                .capacidadeNormal(rs.getLong("capacidade_populacional"))
                .capacidadeMaxima(rs.getLong("capacidade_populacional_maxima"))
                .createdAt(Optional.ofNullable(rs.getTimestamp("created_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .updatedAt(Optional.ofNullable(rs.getTimestamp("updated_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .build();
    }

    @Override
    public void truncate() {
        String query = "TRUNCATE TABLE polo_fornecedor";

        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
                log.info("Tabela polo_fornecedor truncada com sucesso");
            } catch (Exception e) {
                log.error("Erro ao truncar tabela polo_fornecedor", e);
            }
        } catch (SQLException e) {
            log.error("Erro ao conectar ao banco de dados", e);
        }
    }
}
