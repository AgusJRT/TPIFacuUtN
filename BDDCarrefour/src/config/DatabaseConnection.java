package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String CONFIG_FILE = "dbconfig.properties";

    public static Connection getConnection() {
        Properties properties = new Properties();

        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("No se pudo encontrar " + CONFIG_FILE);
                return null;
            }

            properties.load(input);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            // Registrar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Intentar establecer la conexión
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión a la base de datos establecida correctamente.");
            return conn;

        } catch (IOException e) {
            System.err.println("Error al leer el archivo de configuración: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver MySQL no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Connection connection = getConnection();
        if (connection != null) {
            System.out.println("Conexión exitosa a la base de datos.");
        } else {
            System.out.println("Error al conectar a la base de datos.");
        }
    }
}
