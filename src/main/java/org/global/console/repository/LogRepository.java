package org.global.console.repository;

import org.global.console.infra.DataSource;
import org.global.console.model.Log;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogRepository {

    private DataSource dataSource;
    private static LogRepository instance;

    private LogRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static synchronized LogRepository getInstance() {
        if (instance == null) {
            instance = new LogRepository(DataSource.getInstance());
        }

        return instance;
    }

    public void save(Log log) throws SQLException {
        String sql = "INSERT INTO log (tipo, descricao, usuario_id, object_id, object_class) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setString(1, log.getTipo());
            statement.setString(2, log.getDescricao());
            statement.setLong(3, log.getUsuarioId());
            statement.setString(4, log.getObjectId());
            statement.setString(5, log.getObjectClass());

            statement.executeUpdate();
        }
    }
}
