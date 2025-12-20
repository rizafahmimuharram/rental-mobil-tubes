package com.rental.dao;

import com.rental.model.master.Mobil;
import com.rental.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MobilDAO implements MobilDAOInterface {

    private Connection connection = DatabaseConnection.getConnection();

    @Override
    public void save(Mobil mobil) throws SQLException {
        // DISESUAIKAN: Menggunakan nama kolom camelCase sesuai DB kamu
        String sql = "INSERT INTO mobil (jumlahKursi, tipeMesin, merek, model, nomorPlat, status, hargaSewaPerHari) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, mobil.getJumlahKursi());
            pstmt.setString(2, mobil.getTipeMesin());
            pstmt.setString(3, mobil.getMerek());
            pstmt.setString(4, mobil.getModel());
            pstmt.setString(5, mobil.getNomorPlat());
            pstmt.setString(6, mobil.getStatus());
            pstmt.setDouble(7, mobil.getHargaSewaPerHari());
            
            pstmt.executeUpdate();
            System.out.println("Data Mobil " + mobil.getNomorPlat() + " berhasil ditambahkan.");
        }
    }

    @Override
    public Mobil findById(int id) throws SQLException {
        // DISESUAIKAN: id_mobil
        String sql = "SELECT * FROM mobil WHERE id_mobil = ?";
        Mobil mobil = null;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    mobil = buildMobil(rs);
                }
            }
        }
        return mobil;
    }

    @Override
    public List<Mobil> findAll() throws SQLException {
        List<Mobil> listMobil = new ArrayList<>();
        String sql = "SELECT * FROM mobil";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                listMobil.add(buildMobil(rs));
            }
        }
        return listMobil;
    }

    @Override
    public void update(Mobil mobil) throws SQLException {
        // DISESUAIKAN: Nama kolom camelCase dan primary key id_mobil
        String sql = "UPDATE mobil SET jumlahKursi=?, tipeMesin=?, merek=?, model=?, nomorPlat=?, status=?, hargaSewaPerHari=? WHERE id_mobil=?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, mobil.getJumlahKursi());
            pstmt.setString(2, mobil.getTipeMesin());
            pstmt.setString(3, mobil.getMerek());
            pstmt.setString(4, mobil.getModel());
            pstmt.setString(5, mobil.getNomorPlat());
            pstmt.setString(6, mobil.getStatus());
            pstmt.setDouble(7, mobil.getHargaSewaPerHari());
            pstmt.setInt(8, mobil.getId());
            
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        // DISESUAIKAN: id_mobil
        String sql = "DELETE FROM mobil WHERE id_mobil = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Mobil> findAvailableMobil() throws SQLException {
        List<Mobil> availableList = new ArrayList<>();
        String sql = "SELECT * FROM mobil WHERE status = 'Ready'";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                availableList.add(buildMobil(rs));
            }
        }
        return availableList;
    }

    // Helper method agar tidak nulis rs.get berkali-kali
    private Mobil buildMobil(ResultSet rs) throws SQLException {
        return new Mobil(
            rs.getInt("id_mobil"),
            rs.getInt("jumlahKursi"),
            rs.getString("tipeMesin"),
            rs.getString("merek"),
            rs.getString("model"),
            rs.getString("nomorPlat"),
            rs.getString("status"),
            rs.getDouble("hargaSewaPerHari")
        );
    }
    
    
    @Override
public void updateStatus(int idMobil, String statusBaru) throws SQLException {
    String sql = "UPDATE mobil SET status = ? WHERE id_mobil = ?";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, statusBaru);
        pstmt.setInt(2, idMobil);
        
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("[INFO] Status Mobil ID " + idMobil + " diperbarui menjadi: " + statusBaru);
        }
    }
}
}