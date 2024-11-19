package org.global.console.repository;

import org.global.console.infra.DataSource;
import org.global.console.model.Usuario;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeAll
    static void beginTransaction(@Mock DataSource dataSource, @Mock Connection connection) throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        connection.setAutoCommit(false);
    }

    @AfterAll
    static void rollbackTransaction(@Mock Connection connection) throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }


    @BeforeEach
    void setUp() throws SQLException {
        usuario = Usuario.builder()
                .login("testLogin")
                .nome("Test User")
                .email("test@example.com")
                .senha("password")
                .build();

        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    void testSave() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        usuarioRepository.save(usuario);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testSaveException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> usuarioRepository.save(usuario));
    }

    @Test
    void testFindByEmail() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("login")).thenReturn("testLogin");
        when(resultSet.getString("nome")).thenReturn("Test User");
        when(resultSet.getString("email")).thenReturn("test@example.com");
        when(resultSet.getString("senha")).thenReturn("password");

        Usuario foundUsuario = usuarioRepository.findByEmail("test@example.com");

        assertNotNull(foundUsuario);
        assertEquals("testLogin", foundUsuario.getLogin());
    }

    @Test
    void testFindByEmailException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> usuarioRepository.findByEmail("test@example.com"));
    }

    @Test
    void testFindByLogin() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("login")).thenReturn("testLogin");
        when(resultSet.getString("nome")).thenReturn("Test User");
        when(resultSet.getString("email")).thenReturn("test@example.com");
        when(resultSet.getString("senha")).thenReturn("password");

        Usuario foundUsuario = usuarioRepository.findByLogin("testLogin");

        assertNotNull(foundUsuario);
        assertEquals("testLogin", foundUsuario.getLogin());
    }

    @Test
    void testFindByLoginException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> usuarioRepository.findByLogin("testLogin"));
    }

    @Test
    void testUpdate() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        usuarioRepository.update(usuario);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testUpdateException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> usuarioRepository.update(usuario));
    }

    @Test
    void testDelete() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        usuarioRepository.delete("testLogin");

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testDeleteException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> usuarioRepository.delete("testLogin"));
    }

    @Test
    void testFindAll() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("login")).thenReturn("testLogin");
        when(resultSet.getString("nome")).thenReturn("Test User");
        when(resultSet.getString("email")).thenReturn("test@example.com");
        when(resultSet.getString("senha")).thenReturn("password");

        List<Usuario> usuarios = usuarioRepository.findAll();

        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
    }

    @Test
    void testFindAllException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> usuarioRepository.findAll());
    }
}