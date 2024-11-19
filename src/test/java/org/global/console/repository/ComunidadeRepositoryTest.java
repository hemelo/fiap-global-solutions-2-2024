package org.global.console.repository;

import org.global.console.infra.DataSource;
import org.global.console.model.Comunidade;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComunidadeRepositoryTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ComunidadeRepository comunidadeRepository;

    private Comunidade comunidade;


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
        comunidade = Comunidade.builder()
                .id(1L)
                .nome("Comunidade Teste")
                .localizacao("Local Teste")
                .descricao("Descrição Teste")
                .latitude(10.0)
                .longitude(20.0)
                .populacao(100L)
                .build();

        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    void testSave() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        Comunidade savedComunidade = comunidadeRepository.save(comunidade);

        assertNotNull(savedComunidade);
        assertEquals(1L, savedComunidade.getId());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testSaveException() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> comunidadeRepository.save(comunidade));
    }

    @Test
    void testFindById() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("nome")).thenReturn("Comunidade Teste");
        when(resultSet.getString("localizacao")).thenReturn("Local Teste");
        when(resultSet.getString("descricao")).thenReturn("Descrição Teste");
        when(resultSet.getDouble("latitude")).thenReturn(10.0);
        when(resultSet.getDouble("longitude")).thenReturn(20.0);
        when(resultSet.getLong("populacao")).thenReturn(100L);

        Comunidade foundComunidade = comunidadeRepository.findById(1L);

        assertNotNull(foundComunidade);
        assertEquals(1L, foundComunidade.getId());
    }

    @Test
    void testFindByIdException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> comunidadeRepository.findById(1L));
    }

    @Test
    void testFindAll() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("nome")).thenReturn("Comunidade Teste");
        when(resultSet.getString("localizacao")).thenReturn("Local Teste");
        when(resultSet.getString("descricao")).thenReturn("Descrição Teste");
        when(resultSet.getDouble("latitude")).thenReturn(10.0);
        when(resultSet.getDouble("longitude")).thenReturn(20.0);
        when(resultSet.getLong("populacao")).thenReturn(100L);

        List<Comunidade> comunidades = comunidadeRepository.findAll();

        assertNotNull(comunidades);
        assertEquals(1, comunidades.size());
    }

    @Test
    void testFindAllException() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> comunidadeRepository.findAll());
    }

    @Test
    void testUpdate() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Comunidade updatedComunidade = comunidadeRepository.update(comunidade);

        assertNotNull(updatedComunidade);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testUpdateException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> comunidadeRepository.update(comunidade));
    }

    @Test
    void testDelete() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean isDeleted = comunidadeRepository.delete(1L);

        assertTrue(isDeleted);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testDeleteException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> comunidadeRepository.delete(1L));
    }
}