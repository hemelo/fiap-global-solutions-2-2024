package org.global.console.infra;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.internal.database.base.Database;
import org.global.console.exceptions.SistemaException;
import org.global.console.properties.DatabaseProperties;

import java.sql.Connection;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataSource {

    public final AtomicBoolean isInitializing = new AtomicBoolean(false);
    public final AtomicBoolean isRetrievingConnection = new AtomicBoolean(false);

    private HikariDataSource dataSource;
    private static DataSource instance;

    private DataSource()  {

        synchronized (isInitializing) {
            if (isInitializing.get()) {
                throw new SistemaException("Base de dados já está sendo inicializada");
            }

            isInitializing.set(true);
            isInitializing.notifyAll();
        }

        try {
            dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(DatabaseProperties.getInstance().getProperty("database.url"));
            dataSource.setUsername(DatabaseProperties.getInstance().getProperty("database.username"));
            dataSource.setPassword(DatabaseProperties.getInstance().getProperty("database.password"));
            dataSource.setMaximumPoolSize(10);
            dataSource.setMinimumIdle(2);
            dataSource.setConnectionTimeout(30000);
            dataSource.setIdleTimeout(600000);
            dataSource.setMaxLifetime(1800000);
            dataSource.validate();
        } catch (SistemaException e) {
            throw e;
        } catch (Exception e) {
            throw new SistemaException("Erro ao inicializar a base de dados", e);
        } finally {

            synchronized (isInitializing) {
                isInitializing.set(false);
                isInitializing.notifyAll();
            }
        }
    }

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }

        return instance;
    }

    public Connection getConnection() {

        synchronized (isRetrievingConnection) {
            if (isRetrievingConnection.get()) {
                throw new SistemaException("Conexão com a base de dados já está sendo recuperada");
            }

            isRetrievingConnection.set(true);
            isRetrievingConnection.notifyAll();
        }

        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new SistemaException("Erro ao recuperar conexão com a base de dados", e);
        } finally {

            synchronized (isRetrievingConnection) {
                isRetrievingConnection.set(false);
                isRetrievingConnection.notifyAll();
            }
        }
    }

}
