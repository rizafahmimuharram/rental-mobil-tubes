package com.rental.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    

    private static final String URL = "jdbc:mysql://localhost:3306/db_rental_mobil";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 
    
    private static Connection connection = null;


    
    public static Connection getConnection() {
        if (connection == null) {
            try {
               
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Koneksi database berhasil!");
                
            } catch (SQLException e) {
                System.err.println("Koneksi database gagal!");
                e.printStackTrace();
                
                throw new RuntimeException("Gagal terhubung ke database.", e);
            }
        }
        return connection;
    }
    
   
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; 
                System.out.println("Koneksi database ditutup.");
            } catch (SQLException e) {
                System.err.println("Gagal menutup koneksi database.");
                e.printStackTrace();
            }
        }
    }
    
   
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Driver dan koneksi siap.");
        }
        closeConnection();
    }
}



