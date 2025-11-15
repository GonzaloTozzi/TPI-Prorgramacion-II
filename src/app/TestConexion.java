package app;

import config.DatabaseConnection;
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Conexion exitosa!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
