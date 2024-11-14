package org.global.console.repository;

import org.global.console.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa o repositório de usuários.
 * Contém métodos para salvar, buscar, atualizar e deletar usuários.
 * É responsável por realizar a comunicação com o banco de dados.
 */
public class UsuarioRepository {
    private final Connection connection;

    public UsuarioRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, permissao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenha());
            statement.setString(4, usuario.getPermissao());

            statement.executeUpdate();
        }
    }

    public Usuario findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUsuario(resultSet);
                } else {
                    return null;
                }
            }
        }
    }

    public Usuario findById(Long id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUsuario(resultSet);
                } else {
                    return null;
                }
            }
        }
    }

    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, permissao = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenha());
            statement.setString(4, usuario.getPermissao());
            statement.setLong(5, usuario.getId());

            statement.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public List<Usuario> findAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                usuarios.add(mapResultSetToUsuario(resultSet));
            }
        }
        return usuarios;
    }

    private Usuario mapResultSetToUsuario(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(resultSet.getLong("id"));
        usuario.setNome(resultSet.getString("nome"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setSenha(resultSet.getString("senha"));
        usuario.setPermissao(resultSet.getString("permissao"));
        return usuario;
    }
}
