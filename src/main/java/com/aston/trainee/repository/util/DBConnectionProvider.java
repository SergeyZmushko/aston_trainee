package com.aston.trainee.repository.util;

import com.aston.trainee.repository.exceptions.ConnectionException;
import com.aston.trainee.repository.exceptions.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import static com.aston.trainee.repository.util.Constants.SOURCE_ERROR;

/**
 * Класс предоставляющий соединение с базой данных
 */
public class DBConnectionProvider {

    /**
     * Строка содержащая путь к файлу с конфигурационными свойствами базы данных.
     */
    private static final String DB_PROPERTIES_FILE = "connectionInfo.properties";
    /**
     * Строка с URL адресом для подключения к базе данных.
     */
    private String url;
    /**
     * Строка с паролем для подключения к базе данных.
     */
    private String username;
    /**
     * Строка с именем пользователя для подключения к базе данных.
     */
    private String password;

    /**
     * Конструктор для создания объекта DBConnectionProvider при помощи передачи параметром в конструктор.
     * @param url - строка с URL адресом для подключения к базе данных;
     * @param username - строка с именем пользователя для подключения к базе данных;
     * @param password - строка с паролем для подключения к базе данных.
     */
    public DBConnectionProvider(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Конструктор для создания объекта DBConnectionProvider путем чтения properties файла с необходимой информацией для
     * подключения.
     */
    public DBConnectionProvider(){
        loadProperties();
    }

    /**
     * Метод для подключения к базе данных.
     * @return объект Connection.
     */
    public Connection getConnection() {
        try {
            Class.forName(Constants.DB_PSQL_DRIVER);
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new ConnectionException(Constants.CONNECTION_ERROR);
        }
    }

    /**
     * Метод читает properties файл и получает необходимые данные для подключения к базе данных.
     */
    private void loadProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE) ) {
            properties.load(inputStream);
            url = properties.getProperty(Constants.DB_URL_PROP);
            username = properties.getProperty(Constants.DB_USER_PROP);
            password = properties.getProperty(Constants.DB_PASSWORD_PROP);
        } catch (IOException e) {
            throw new ParseException(SOURCE_ERROR);
        }
    }
}
