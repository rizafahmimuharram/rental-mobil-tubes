package com.rental.dao;

import com.rental.model.transaksi.Pembayaran;
import com.rental.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;

public class PembayaranDAO implements PembayaranDAOInterface {

    private final Connection connection = DatabaseConnection.getConnection();
    
   
    @Override
    public void save(Pembayaran pembayaran, int bookingId) throws SQLException {
        // Asumsi struktur tabel: pembayaran (id_pembayaran, tgl_pembayaran, metode, jumlah_bayar, status, booking_id)
        String sql = "INSERT INTO pembayaran (tgl_pembayaran, metode, jumlah_bayar, status, booking_id) VALUES (?, ?, ?, ?, ?)";
        int newId = -1;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(pembayaran.getTanggalPembayaran()));
            pstmt.setString(2, pembayaran.getMetode());
            pstmt.setDouble(3, pembayaran.getJumlahBayar());
            pstmt.setString(4, pembayaran.getStatusPembayaran());
            pstmt.setInt(5, bookingId); // Foreign Key ke tabel booking
            
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    newId = rs.getInt(1);
                    pembayaran.setIdPembayaran(newId);
                }
            }
            System.out.println("Pembayaran ID " + newId + " untuk Booking " + bookingId + " berhasil disimpan.");
        }
    }
    
    // ----------------------------------------------------
    // READ (Mencari berdasarkan Kunci Asing Booking ID)
    // ----------------------------------------------------
    @Override
    public Pembayaran findByBookingId(int bookingId) throws SQLException {
        String sql = "SELECT * FROM pembayaran WHERE booking_id = ?";
        Pembayaran pembayaran = null;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    pembayaran = new Pembayaran(
                        rs.getInt("id_pembayaran"),
                        rs.getTimestamp("tgl_pembayaran").toLocalDateTime(),
                        rs.getString("metode"),
                        rs.getDouble("jumlah_bayar"),
                        rs.getString("status")
                    );
                }
            }
        }
        return pembayaran;
    }
    
   
    @Override
    public void updateStatus(int idPembayaran, String statusBaru) throws SQLException {
        String sql = "UPDATE pembayaran SET status = ? WHERE id_pembayaran = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, statusBaru);
            pstmt.setInt(2, idPembayaran);
            pstmt.executeUpdate();
            System.out.println("Status Pembayaran ID " + idPembayaran + " berhasil diubah menjadi: " + statusBaru);
        }
    }
}