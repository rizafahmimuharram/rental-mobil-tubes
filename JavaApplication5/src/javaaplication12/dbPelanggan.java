package javaaplication12;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;




public class dbPelanggan {
    private Connection conn;
    private final Koneksi k = new Koneksi();

    
    public ArrayList<Pelanggan> getPelanggan() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Pelanggan> daftar = new ArrayList<>();
        String kueri = "SELECT * FROM pelanggan"; 

        try {
            
            conn = k.getConnection();
            ps = conn.prepareStatement(kueri);
            
            
            rs = ps.executeQuery();

            
            while (rs.next()) {
                String id = rs.getString("id"); 
                String nama = rs.getString("nama");
                String jenis = rs.getString("jenis");
                String tahun = rs.getString("tahunlahir");

                Pelanggan p = new Pelanggan(id, nama, jenis, Integer.parseInt(tahun));
                daftar.add(p);
            }
        } finally {
            // Langkah 5: Tutup koneksi [cite: 31]
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        return daftar;
    }

   
    public boolean insertPelanggan(Pelanggan p) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        int rowAffected = 0;
        
       
        String kueri = "INSERT INTO pelanggan (id, nama, jenis, tahunlahir) VALUES (?, ?, ?, ?)"; 
        try {
            // Langkah 1: Buat koneksi [cite: 32]
            conn = k.getConnection();
            ps = conn.prepareStatement(kueri);
            
            // Langkah 2: Set parameter kueri [cite: 32]
            ps.setString(1, p.getId());
            ps.setString(2, p.getNama());
            ps.setString(3, p.getJenis());
            ps.setInt(4, p.getTahunLahir()); 
            
            // Langkah 3: Eksekusi kueri [cite: 32]
            rowAffected = ps.executeUpdate();
        } finally {
            // Langkah 4 & 5: Tutup koneksi [cite: 32]
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        return rowAffected == 1; // [cite: 32]
    }

   
    public boolean updatePelanggan(Pelanggan p) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        int rowAffected = 0;
        
        
        String kueri = "UPDATE pelanggan SET nama=?, jenis=?, tahunlahir=? WHERE id=?"; // Disesuaikan dengan struktur tabel [cite: 33, 24]

        try {
            
            conn = k.getConnection();
            ps = conn.prepareStatement(kueri);
            
            // Langkah 2: Set parameter kueri [cite: 33]
            ps.setString(1, p.getNama());
            ps.setString(2, p.getJenis());
            ps.setInt(3, p.getTahunLahir());
            ps.setString(4, p.getId()); // ID digunakan di WHERE
            
            // Langkah 3: Eksekusi kueri [cite: 33]
            rowAffected = ps.executeUpdate();
        } finally {
            // Langkah 4 & 5: Tutup koneksi [cite: 33]
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        return rowAffected == 1; // [cite: 33]
    }

    
    public boolean deletePelanggan(String id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        int rowAffected = 0;
        
        // Kueri: DELETE FROM pelanggan WHERE id=?
        String kueri = "DELETE FROM pelanggan WHERE id=?"; // Disesuaikan dengan struktur tabel [cite: 34, 24]

        try {
            // Langkah 1: Buat koneksi [cite: 34]
            conn = k.getConnection();
            ps = conn.prepareStatement(kueri);
            
            // Langkah 2: Set parameter kueri [cite: 34]
            ps.setString(1, id);
            
            // Langkah 3: Eksekusi kueri [cite: 34]
            rowAffected = ps.executeUpdate();
        } finally {
            // Langkah 4 & 5: Tutup koneksi [cite: 34]
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        return rowAffected == 1; // [cite: 34]
    }

   
    public ArrayList<Pelanggan> cariPelanggan(String keyword) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Pelanggan> daftar = new ArrayList<>();
        
        
        String kueri = "SELECT * FROM pelanggan WHERE nama LIKE ? OR tahunlahir LIKE ? OR jenis LIKE ?"; // Disesuaikan dengan slide [cite: 35]

        try {
            // Langkah 1: Buat koneksi [cite: 35]
            conn = k.getConnection();
            ps = conn.prepareStatement(kueri);
            
           
            ps.setString(1, "%" + keyword + "%"); // Pencarian nama
            ps.setString(2, keyword);             // Pencarian tahun lahir (diasumsikan tahun dicari eksak atau substring)
            ps.setString(3, keyword);             // Pencarian jenis
            
           
            rs = ps.executeQuery();

            
            while (rs.next()) {
                String id = rs.getString("id"); // Menggunakan nama kolom 'id' dari struktur tabel 
                String nama = rs.getString("nama");
                String jenis = rs.getString("jenis");
                String tahun = rs.getString("tahunlahir");

                Pelanggan p = new Pelanggan(id, nama, jenis, Integer.parseInt(tahun));
                daftar.add(p);
            }
        } finally {
            
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        return daftar;
    }
}