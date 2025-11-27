package com.mycompany.rental_mobil_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/db_rental_mobil";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Koneksi ke database BERHASIL!");
            } catch (SQLException e) {
                System.out.println("Koneksi GAGAL: " + e.getMessage());
            }
        }
        return conn;
    }
}
