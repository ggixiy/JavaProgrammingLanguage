package com.lab11.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimeZone;

public class DatabaseConnection {

    public static final String DEFAULT_PROPERTIES = "db.properties";

    private final String url;
    private final String user;
    private final String password;

    public DatabaseConnection() {
        this(DEFAULT_PROPERTIES);
    }

    public DatabaseConnection(String propertiesFile) {
        Properties props = loadProperties(propertiesFile);
        this.url = require(props, "db.url", propertiesFile);
        this.user = require(props, "db.user", propertiesFile);
        this.password = props.getProperty("db.password", "");

        String timezone = props.getProperty("db.timezone");
        if (timezone != null && !timezone.isBlank()) {
            TimeZone.setDefault(TimeZone.getTimeZone(timezone.trim()));
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public String getUrl() {
        return url;
    }

    private static Properties loadProperties(String fileName) {
        Properties props = new Properties();
        try (InputStream in = DatabaseConnection.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) {
                throw new IllegalStateException("Файл властивостей не знайдено у classpath: " + fileName);
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Не вдалося прочитати файл властивостей: " + fileName, e);
        }
        return props;
    }

    private static String require(Properties props, String key, String fileName) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("У файлі " + fileName + " відсутній параметр " + key);
        }
        return value.trim();
    }
}
