package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    private DatabaseConnection() {
        // Construtor privado para evitar instância externa
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "root");
            }catch(ClassNotFoundException | SQLException ex){
                System.out.println(ex.getMessage());
                System.out.println("Não foi possível conectar!");
                throw new RuntimeException(ex);
            }
        }
        return connection;
    }
}