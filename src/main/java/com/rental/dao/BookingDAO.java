package com.rental.dao;

import com.rental.model.master.Mobil;
import com.rental.model.transaksi.Booking;
import com.rental.model.user.Pelanggan;
import com.rental.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO implements BookingDAOInterface {
    private final Connection connection = DatabaseConnection.getConnection();
    private final MobilDAO mobilDAO = new MobilDAO();
    private final UserDAO userDAO = new UserDAO();

    private Booking buildBooking(ResultSet rs) throws SQLException {
        int bookingId = rs.getInt("id_booking");
        int mobilId = rs.getInt("mobil_id");
        int pelangganId = rs.getInt("pelanggan_id");

        Mobil m = mobilDAO.findById(mobilId);
        Pelanggan p = (Pelanggan) userDAO.findById(pelangganId, "pelanggan"); 

        Booking booking = new Booking(
            bookingId,
            rs.getDate("tanggalMulai").toLocalDate(),
            rs.getDate("tanggalSelesai").toLocalDate(),
            m,
            p
        );

        booking.setStatus(rs.getString("status"));
        booking.setTotalBiaya(rs.getDouble("totalBiaya"));
        
       
        
        return booking;
    }

    @Override
    public void save(Booking booking) throws SQLException {     
        String sql = "INSERT INTO booking (tanggalBooking, tanggalMulai, tanggalSelesai, status, totalBiaya, waktuBatasBayar, pelanggan_id, mobil_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, java.sql.Date.valueOf(booking.getTanggalBooking()));
            pstmt.setDate(2, java.sql.Date.valueOf(booking.getTanggalMulai()));
            pstmt.setDate(3, java.sql.Date.valueOf(booking.getTanggalSelesai()));
            pstmt.setString(4, booking.getStatus());
            pstmt.setDouble(5, booking.getTotalBiaya());
            pstmt.setTimestamp(6, java.sql.Timestamp.valueOf(booking.getWaktuBatasBayar()));
            pstmt.setInt(7, booking.getPelanggan().getId()); // pelanggan_id
            pstmt.setInt(8, booking.getMobil().getId());    // mobil_id

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    booking.setIdBooking(rs.getInt(1));
                }
            }
            System.out.println("[SUCCESS] Booking ID " + booking.getIdBooking() + " berhasil disimpan ke Database.");
        }
    }

    @Override
    public Booking findById(int id) throws SQLException {
        String sql = "SELECT * FROM booking WHERE id_booking = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return buildBooking(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Booking> findAll() throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking ORDER BY id_booking DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(buildBooking(rs));
            }
        }
        return list;
    }

    @Override
    public List<Booking> findPendingBookings() throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE status = 'Pending'";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(buildBooking(rs));
            }
        }
        return list;
    }

    @Override
    public void updateStatus(int bookingId, String statusBaru) throws SQLException {
        String sql = "UPDATE booking SET status = ? WHERE id_booking = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, statusBaru);
            pstmt.setInt(2, bookingId);
            pstmt.executeUpdate();
            System.out.println("[INFO] Status Booking ID " + bookingId + " diupdate menjadi: " + statusBaru);
        }
    }

    @Override
    public void update(Booking booking) throws SQLException {
        // Update menggunakan nama kolom totalBiaya sesuai DB
        String sql = "UPDATE booking SET status = ?, totalBiaya = ? WHERE id_booking = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, booking.getStatus());
            pstmt.setDouble(2, booking.getTotalBiaya());
            pstmt.setInt(3, booking.getIdBooking());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM booking WHERE id_booking = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("[INFO] Booking ID " + id + " telah dihapus.");
        }
    }
    
    
@Override
public List<Booking> findActiveBookings() throws SQLException {
    List<Booking> list = new ArrayList<>();
    // Kita mencari booking yang sudah 'Approved' (mobil sedang dibawa pelanggan)
    String sql = "SELECT * FROM booking WHERE status = 'Approved'";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            list.add(buildBooking(rs));
        }
    }
    return list;
}

@Override
public List<Booking> findBookingsByPelanggan(int pelangganId) throws SQLException {
    List<Booking> list = new ArrayList<>();
    // Menggunakan kolom pelanggan_id sesuai struktur DB kamu
    String sql = "SELECT * FROM booking WHERE pelanggan_id = ? ORDER BY id_booking DESC";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, pelangganId);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(buildBooking(rs));
            }
        }
    }
    return list;
}


@Override
public List<Booking> findByStatus(String status) throws SQLException {
    List<Booking> list = new ArrayList<>();
    String sql = "SELECT * FROM booking WHERE status = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, status);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(buildBooking(rs));
            }
        }
    }
    return list;
}

}