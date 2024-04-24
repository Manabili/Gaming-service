package com.game.services.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseClient {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseClient.class);
    private static DatabaseClient instance = null;

    private DatabaseClient() {
        // Initialize the database driver, if needed
        try {
            Class.forName(Constants.MYSQL_DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.error("Error initializing Database Driver !");
        }
    }

    // Method to get the single instance of the class
    public static DatabaseClient getInstance() {
        if (instance == null) {
            instance = new DatabaseClient();
        }
        return instance;
    }

    public Connection getConnection(String database) throws SQLException {
        // Establish a connection to the database
        String dbUrl = Constants.DATABASE_URL + database;
        return DriverManager.getConnection(dbUrl, Constants.USERNAME, Constants.PASSWORD);
    }

}
