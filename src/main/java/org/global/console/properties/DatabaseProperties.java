package org.global.console.properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.global.console.exceptions.SistemaException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class DatabaseProperties {

    private Properties properties = new Properties();
    private static DatabaseProperties instance;

    private DatabaseProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IOException("Unable to find database.properties");
            }
            properties.load(input);

            if (properties.isEmpty()) {
                throw new IOException("database.properties is empty");
            }

            if (StringUtils.isBlank(properties.getProperty("database.url"))) {
                throw new IOException("database.url is missing");
            }

            if (StringUtils.isBlank(properties.getProperty("database.username"))) {
                throw new IOException("database.username is missing");
            }

            if (StringUtils.isBlank(properties.getProperty("database.password"))) {
                throw new IOException("database.password is missing");
            }

        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new SistemaException("Não foi possível carregar o arquivo de propriedades do banco de dados", ex);
        }
    }

    public static synchronized DatabaseProperties getInstance() {

        if (instance == null) {
            instance = new DatabaseProperties();
        }

        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}