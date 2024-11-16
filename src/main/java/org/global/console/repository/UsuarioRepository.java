package org.global.console.repository;

import lombok.extern.slf4j.Slf4j;
import org.global.console.infra.DataSource;
import org.global.console.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe que representa o repositório de usuários.
 * Contém métodos para salvar, buscar, atualizar e deletar usuários.
 * É responsável por realizar a comunicação com o banco de dados.
 */
@Slf4j
public class UsuarioRepository {

    private static UsuarioRepository instance;
    private final DataSource dataSource;

    private UsuarioRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static synchronized UsuarioRepository getInstance() {
        if (instance == null) {
            instance = new UsuarioRepository(DataSource.getInstance());
        }

        return instance;
    }

    public void save(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, login) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenha());
            statement.setString(5, usuario.getLogin());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Erro ao salvar usuário", e);
            throw e;
        }
    }

    public Usuario findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUsuario(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            log.error("Erro ao buscar usuário por email", e);
            throw e;
        }
    }

    public Usuario findByLogin(String login) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE login = ?";
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setString(1, login);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUsuario(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            log.error("Erro ao buscar usuário por login", e);
            throw e;
        }
    }

    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE login = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenha());
            statement.setString(4, usuario.getLogin());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Erro ao atualizar usuário", e);
            throw e;
        }
    }

    public void delete(String login) throws SQLException {
        String sql = "DELETE FROM usuario WHERE login = ?";
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setString(1, login);
            statement.executeUpdate();
        }
    }

    public List<Usuario> findAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                usuarios.add(mapResultSetToUsuario(resultSet));
            }
        } catch (SQLException e) {
            log.error("Erro ao buscar usuários", e);
            throw e;
        }

        return usuarios;
    }

    private Usuario mapResultSetToUsuario(ResultSet resultSet) throws SQLException {
        return Usuario.builder()
                .login(resultSet.getString("login"))
                .nome(resultSet.getString("nome"))
                .email(resultSet.getString("email"))
                .senha(resultSet.getString("senha"))
                .createdAt(Optional.ofNullable(resultSet.getTimestamp("created_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .updatedAt(Optional.ofNullable(resultSet.getTimestamp("updated_at")).map(Timestamp::toLocalDateTime).orElse(null))
                .build();
    }
}
