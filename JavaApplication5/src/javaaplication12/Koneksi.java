package javaaplication12;

import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.cj.jdbc.MysqlDataSource;

/**
 *
 * @author xiada
 */
public class Koneksi {
    private final MysqlDataSource dataSource = new MysqlDataSource();
    
    // PERBAIKAN 1 & 2: Port 3306 dan Database Penjualan
    private final String DB_URL = "jdbc:mysql://localhost:3306/Penjualan?serverTimezone=Asia/Jakarta"; 
    
    private final String DB_USERNAME = "root";
    
    // PERBAIKAN 3: Password disetel menjadi string kosong ("")
    private final String DB_PASSWORD = ""; 
    
    public Connection getConnection() throws SQLException {
     dataSource.setUrl(DB_URL);
     dataSource.setUser(DB_USERNAME);
     dataSource.setPassword(DB_PASSWORD);
     Connection conn = dataSource.getConnection();
     
     return conn;
    }
}