package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.ConnectionFactory;

public class ConnectionTest {

    public static void main(String[] args) {
        System.out.println("=== DB Connection Test Start ===");

        try (Connection conn = ConnectionFactory.getConnection()) {
            System.out.println("Connected: " + !conn.isClosed());

            // users件数確認
            System.out.println("[users]");
            countTable(conn, "users");

            // tasks件数確認
            System.out.println("[tasks]");
            countTable(conn, "tasks");

            System.out.println("=== DB Connection Test Success ===");

        } catch (SQLException e) {
            System.err.println("DB error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void countTable(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("count = " + rs.getInt(1));
            }
        }
    }
}
