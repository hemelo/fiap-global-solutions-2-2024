package org.global.console.repository;

import org.global.console.infra.DataSource;
import org.global.console.model.Fornecedor;
import org.global.console.model.PoloFornecedor;
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
public class PoloFornecedorRepositoryTest {

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

    @Mock
    private FornecedorRepository fornecedorRepository;

    @InjectMocks
    private PoloFornecedorRepository poloFornecedorRepository;

    private PoloFornecedor poloFornecedor;
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

        poloFornecedor = PoloFornecedor.builder()
                .id(1L)
                .nome("Polo Fornecedor Teste")
                .endereco("Local Teste")
                .fornecedorId(fornecedor.getId())
                .latitude(10.0)
                .longitude(20.0)
                .build();

        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void testSave() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        PoloFornecedor savedPoloFornecedor = poloFornecedorRepository.save(poloFornecedor);

        assertNotNull(savedPoloFornecedor);
        assertEquals(1L, savedPoloFornecedor.getId());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testSaveException() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> poloFornecedorRepository.save(poloFornecedor));
    }

    @Test
    public void testFindById() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("nome")).thenReturn("Polo Fornecedor Teste");
        when(resultSet.getString("endereco")).thenReturn("Local Teste");
        when(resultSet.getLong("fornecedor_id")).thenReturn(1L);
        when(resultSet.getDouble("latitude")).thenReturn(10.0);
        when(resultSet.getDouble("longitude")).thenReturn(20.0);

        PoloFornecedor foundPoloFornecedor = poloFornecedorRepository.findById(1L);

        assertNotNull(foundPoloFornecedor);
        assertEquals(1L, foundPoloFornecedor.getId());
    }

    @Test
    public void testFindByIdException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> poloFornecedorRepository.findById(1L));
    }

    @Test
    public void testFindAll() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("nome")).thenReturn("Polo Fornecedor Teste");
        when(resultSet.getString("endereco")).thenReturn("Local Teste");
        when(resultSet.getLong("fornecedor_id")).thenReturn(1L);
        when(resultSet.getDouble("latitude")).thenReturn(10.0);
        when(resultSet.getDouble("longitude")).thenReturn(20.0);

        List<PoloFornecedor> poloFornecedores = poloFornecedorRepository.findAll();

        assertNotNull(poloFornecedores);
        assertEquals(1, poloFornecedores.size());
    }

    @Test
    public void testFindAllException() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> poloFornecedorRepository.findAll());
    }

    @Test
    public void testUpdate() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        poloFornecedorRepository.update(poloFornecedor);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> poloFornecedorRepository.update(poloFornecedor));
    }

    @Test
    public void testDelete() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        poloFornecedorRepository.delete(1L);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> poloFornecedorRepository.delete(1L));
    }
}