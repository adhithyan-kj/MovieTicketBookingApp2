package movieticketbookinggui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Manages a single connection, reading credentials from an external file (db_config.txt)
 * which should NOT be committed to Git.
 */
public class DBConnection {
    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());

    // Default values if config file is not found (for development use)
    private static String DB_URL = "jdbc:mysql://localhost:3306/movie_booking_db";
    private static String DB_USER = "root";
    private static String DB_PASS = "default_password_if_file_missing";

    private static Connection connection = null;

    // Static block to read the file upon class loading
    static {
        readDbConfig();
    }

    private static void readDbConfig() {
        // Reads configuration from the root directory of the project
        try (BufferedReader reader = new BufferedReader(new FileReader("db_config.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("DB_URL")) {
                    DB_URL = line.substring(line.indexOf('=') + 1).trim();
                } else if (line.startsWith("DB_USER")) {
                    DB_USER = line.substring(line.indexOf('=') + 1).trim();
                } else if (line.startsWith("DB_PASS")) {
                    DB_PASS = line.substring(line.indexOf('=') + 1).trim();
                }
            }
            logger.info("Database configuration loaded from db_config.txt.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "ERROR: db_config.txt file not found. Using hardcoded defaults.", e);
        }
    }

    /**
     * Gets a connection to the database, establishing it if one does not exist.
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Ensures the MySQL driver is registered/loaded
                Class.forName("com.mysql.cj.jdbc.Driver");
                logger.info("Attempting to connect to database: " + DB_URL);

                // Attempts to establish the connection
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                logger.info("Database connection established successfully.");

            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, "MySQL JDBC Driver not found!", e);
                JOptionPane.showMessageDialog(null, "FATAL ERROR: Database Driver Missing.", "Database Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to connect to database. Check server status/credentials.", e);
                JOptionPane.showMessageDialog(null, "FATAL ERROR: Could not connect to MySQL Server. Check your 'db_config.txt' file.", "Database Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
        return connection;
    }
}
