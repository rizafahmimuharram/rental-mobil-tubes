package com.rental.dao;

import com.rental.model.user.Admin;
import com.rental.model.user.Pelanggan;
import com.rental.model.user.Petugas;
import com.rental.model.user.User;
import com.rental.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements UserDAOInterface {

    private final Connection connection = DatabaseConnection.getConnection();

    // SINKRON: Urutan parameter disesuaikan dengan Constructor Pelanggan.java (alamat dulu, baru no_wa)
    private Pelanggan buildPelanggan(ResultSet rs, int userId, String nama, String email, String password) throws SQLException {
        return new Pelanggan(
            userId, nama, email, password,
            rs.getString("alamat"), 
            rs.getString("no_wa"),  
            rs.getString("no_ktp")
        );
    }

    // SINKRON: Urutan parameter disesuaikan dengan Constructor Petugas.java
    private Petugas buildPetugas(ResultSet rs, int userId, String nama, String email, String password) throws SQLException {
        return new Petugas(
            userId, nama, email, password,
            rs.getString("no_telp"),
            rs.getString("shift")
        );
    }

    @Override
    public User login(String email, String password) throws SQLException {
        String baseSql = "SELECT id, nama, password, role FROM user WHERE email = ? AND password = ?";
        User user = null;

        try (PreparedStatement pstmtBase = connection.prepareStatement(baseSql)) {
            pstmtBase.setString(1, email);
            pstmtBase.setString(2, password);
            
            try (ResultSet rsBase = pstmtBase.executeQuery()) {
                if (rsBase.next()) {
                    int userId = rsBase.getInt("id");
                    String nama = rsBase.getString("nama");
                    String pass = rsBase.getString("password");
                    String role = rsBase.getString("role");
                    
                    switch (role.toLowerCase()) {
                        case "pelanggan":
                            String sqlPelanggan = "SELECT * FROM pelanggan_details WHERE user_id = ?";
                            try (PreparedStatement pstmtDetail = connection.prepareStatement(sqlPelanggan)) {
                                pstmtDetail.setInt(1, userId);
                                try (ResultSet rs = pstmtDetail.executeQuery()) {
                                    if (rs.next()) user = buildPelanggan(rs, userId, nama, email, pass);
                                }
                            }
                            break;
                            
                        case "petugas":
                            String sqlPetugas = "SELECT * FROM petugas_details WHERE user_id = ?";
                            try (PreparedStatement pstmtDetail = connection.prepareStatement(sqlPetugas)) {
                                pstmtDetail.setInt(1, userId);
                                try (ResultSet rs = pstmtDetail.executeQuery()) {
                                    if (rs.next()) user = buildPetugas(rs, userId, nama, email, pass);
                                }
                            }
                            break;
                            
                        case "admin":
                            user = new Admin(userId, nama, email, pass);
                            break;
                    }
                    System.out.println("Login berhasil sebagai: " + role);
                }
            }
        }
        return user;
    }

    @Override
    public void save(User user) throws SQLException {
        String baseSql = "INSERT INTO user (nama, email, password, role) VALUES (?, ?, ?, ?)";
        int newId = -1;
        String role = (user instanceof Pelanggan) ? "pelanggan" : (user instanceof Petugas ? "petugas" : "admin");

        try (PreparedStatement pstmtBase = connection.prepareStatement(baseSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmtBase.setString(1, user.getNama());
            pstmtBase.setString(2, user.getEmail());
            pstmtBase.setString(3, user.getPassword());
            pstmtBase.setString(4, role);
            pstmtBase.executeUpdate();
            
            try (ResultSet rs = pstmtBase.getGeneratedKeys()) {
                if (rs.next()) {
                    newId = rs.getInt(1);
                    user.setId(newId); 
                }
            }
        }
        
        if (newId != -1) {
            if (user instanceof Pelanggan p) {
                String detailSql = "INSERT INTO pelanggan_details (user_id, alamat, no_wa, no_ktp) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmtDetail = connection.prepareStatement(detailSql)) {
                    pstmtDetail.setInt(1, newId);
                    pstmtDetail.setString(2, p.getAlamat());
                    pstmtDetail.setString(3, p.getNoWa()); // Pastikan di Pelanggan.java nama metodenya getNo_wa
                    pstmtDetail.setString(4, p.getNoKtp());
                    pstmtDetail.executeUpdate();
                }
            } else if (user instanceof Petugas pt) {
                String detailSql = "INSERT INTO petugas_details (user_id, no_telp, shift) VALUES (?, ?, ?)";
                try (PreparedStatement pstmtDetail = connection.prepareStatement(detailSql)) {
                    pstmtDetail.setInt(1, newId);
                    pstmtDetail.setString(2, pt.getNoTelp());
                    pstmtDetail.setString(3, pt.getShift());
                    pstmtDetail.executeUpdate();
                }
            }
            System.out.println("User " + user.getNama() + " (" + role + ") berhasil didaftarkan.");
        }
    }

    @Override
    public void update(User user) throws SQLException {
        String baseSql = "UPDATE user SET nama=?, email=?, password=? WHERE id=?";
   
        try (PreparedStatement pstmtBase = connection.prepareStatement(baseSql)) {
            pstmtBase.setString(1, user.getNama());
            pstmtBase.setString(2, user.getEmail());
            pstmtBase.setString(3, user.getPassword());
            pstmtBase.setInt(4, user.getId());
            pstmtBase.executeUpdate();
        }
        
        if (user instanceof Pelanggan p) {
            String detailSql = "UPDATE pelanggan_details SET alamat=?, no_wa=?, no_ktp=? WHERE user_id=?";
            try (PreparedStatement pstmtDetail = connection.prepareStatement(detailSql)) {
                pstmtDetail.setString(1, p.getAlamat());
                pstmtDetail.setString(2, p.getNoWa());
                pstmtDetail.setString(3, p.getNoKtp());
                pstmtDetail.setInt(4, p.getId());
                pstmtDetail.executeUpdate();
            }
        } else if (user instanceof Petugas pt) {
            String detailSql = "UPDATE petugas_details SET no_telp=?, shift=? WHERE user_id=?";
            try (PreparedStatement pstmtDetail = connection.prepareStatement(detailSql)) {
                pstmtDetail.setString(1, pt.getNoTelp());
                pstmtDetail.setString(2, pt.getShift());
                pstmtDetail.setInt(3, pt.getId());
                pstmtDetail.executeUpdate();
            }
        }
        System.out.println("Data User ID " + user.getId() + " diperbarui.");
    }

    @Override
    public void delete(int id, String role) throws SQLException {
        // 1. Hapus di tabel detail terlebih dahulu berdasarkan role
        String detailSql = "";
        if (role.equalsIgnoreCase("pelanggan")) {
            detailSql = "DELETE FROM pelanggan_details WHERE user_id = ?";
        } else if (role.equalsIgnoreCase("petugas")) {
            detailSql = "DELETE FROM petugas_details WHERE user_id = ?";
        }

        if (!detailSql.isEmpty()) {
            try (PreparedStatement pstmtDetail = connection.prepareStatement(detailSql)) {
                pstmtDetail.setInt(1, id);
                pstmtDetail.executeUpdate();
            }
        }

        
        String baseSql = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement pstmtBase = connection.prepareStatement(baseSql)) {
            pstmtBase.setInt(1, id);
            pstmtBase.executeUpdate();
        }
    }

    @Override
    public User findById(int id, String role) throws SQLException {
        String sql = "SELECT u.*, d.* FROM user u ";
        if(role.equalsIgnoreCase("pelanggan")) {
            sql += "JOIN pelanggan_details d ON u.id = d.user_id WHERE u.id = ?";
        } else if(role.equalsIgnoreCase("petugas")) {
            sql += "JOIN petugas_details d ON u.id = d.user_id WHERE u.id = ?";
        } else {
            sql = "SELECT * FROM user WHERE id = ?";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nama = rs.getString("nama");
                    String email = rs.getString("email");
                    String pass = rs.getString("password");
                    if (role.equalsIgnoreCase("pelanggan")) return buildPelanggan(rs, id, nama, email, pass);
                    if (role.equalsIgnoreCase("petugas")) return buildPetugas(rs, id, nama, email, pass);
                    return new Admin(id, nama, email, pass);
                }
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> userList = new ArrayList<>();
        // Contoh: Mengambil semua pelanggan saja (bisa disesuaikan jika ingin semua role)
        String sql = "SELECT u.*, pd.* FROM user u JOIN pelanggan_details pd ON u.id = pd.user_id WHERE u.role = 'pelanggan'";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                 userList.add(buildPelanggan(rs, rs.getInt("id"), rs.getString("nama"), rs.getString("email"), rs.getString("password")));
            }
        }
        return userList;
    }
}