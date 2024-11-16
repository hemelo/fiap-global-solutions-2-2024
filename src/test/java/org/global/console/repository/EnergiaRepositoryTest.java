package org.global.console.repository;

import org.global.console.infra.DataSource;
import org.global.console.model.Energia;
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
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnergiaRepositoryTest {

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
    private EnergiaRepository energiaRepository;

    private Energia energia;

    @BeforeAll
    public static void beginTransaction(@Mock DataSource dataSource, @Mock Connection connection) throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        connection.setAutoCommit(false);
    }

    @AfterAll
    public static void rollbackTransaction(@Mock Connection connection) throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    @BeforeEach
    public void setUp() throws SQLException {
        energia = Energia.builder()
                .id(1L)
                .tipo("Solar")
                .nome("Energia Solar")
                .descricao("Energia obtida a partir do sol")
                .build();

        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void testSave() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        Energia savedEnergia = energiaRepository.save(energia);

        assertNotNull(savedEnergia);
        assertEquals(1L, savedEnergia.getId());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testSaveException() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> energiaRepository.save(energia));
    }

    @Test
    public void testFindById() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("tipo")).thenReturn("Solar");
        when(resultSet.getString("nome")).thenReturn("Energia Solar");
        when(resultSet.getString("descricao")).thenReturn("Energia obtida a partir do sol");

        Energia foundEnergia = energiaRepository.findById(1L);

        assertNotNull(foundEnergia);
        assertEquals(1L, foundEnergia.getId());
    }

    @Test
    public void testFindByIdException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> energiaRepository.findById(1L));
    }

    @Test
    public void testFindAll() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("tipo")).thenReturn("Solar");
        when(resultSet.getString("nome")).thenReturn("Energia Solar");
        when(resultSet.getString("descricao")).thenReturn("Energia obtida a partir do sol");

        List<Energia> energias = energiaRepository.findAll();

        assertNotNull(energias);
        assertEquals(1, energias.size());
    }

    @Test
    public void testFindAllException() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> energiaRepository.findAll());
    }

    @Test
    public void testUpdate() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Energia updatedEnergia = energiaRepository.update(energia);

        assertNotNull(updatedEnergia);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> energiaRepository.update(energia));
    }

    @Test
    public void testDelete() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean isDeleted = energiaRepository.delete(1L);

        assertTrue(isDeleted);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> energiaRepository.delete(1L));
    }
}