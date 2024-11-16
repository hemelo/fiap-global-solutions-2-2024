package org.global.console.repository;

import org.global.console.infra.DataSource;
import org.global.console.model.Fornecedor;
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
public class FornecedorRepositoryTest {

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
    private FornecedorRepository fornecedorRepository;

    private Fornecedor fornecedor;

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
        fornecedor = Fornecedor.builder()
                .id(1L)
                .nome("Fornecedor Teste")
                .cnpj("00.000.000/0000-00")
                .descricao("Descrição Teste")
                .build();

        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void testSave() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        Fornecedor savedFornecedor = fornecedorRepository.save(fornecedor);

        assertNotNull(savedFornecedor);
        assertEquals(1L, savedFornecedor.getId());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testSaveException() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> fornecedorRepository.save(fornecedor));
    }

    @Test
    public void testFindById() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("nome")).thenReturn("Fornecedor Teste");
        when(resultSet.getString("cnpj")).thenReturn("00.000.000/0000-00");
        when(resultSet.getString("descricao")).thenReturn("Descrição Teste");

        Fornecedor foundFornecedor = fornecedorRepository.findById(1L);

        assertNotNull(foundFornecedor);
        assertEquals(1L, foundFornecedor.getId());
    }

    @Test
    public void testFindByIdException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> fornecedorRepository.findById(1L));
    }

    @Test
    public void testFindAll() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("nome")).thenReturn("Fornecedor Teste");
        when(resultSet.getString("cnpj")).thenReturn("00.000.000/0000-00");
        when(resultSet.getString("descricao")).thenReturn("Descrição Teste");

        List<Fornecedor> fornecedores = fornecedorRepository.findAll();

        assertNotNull(fornecedores);
        assertEquals(1, fornecedores.size());
    }

    @Test
    public void testFindAllException() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> fornecedorRepository.findAll());
    }

    @Test
    public void testUpdate() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        fornecedorRepository.update(fornecedor);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> fornecedorRepository.update(fornecedor));
    }

    @Test
    public void testDelete() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        fornecedorRepository.delete(1L);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> fornecedorRepository.delete(1L));
    }
}