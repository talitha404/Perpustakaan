package perpustakaan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/perpustakaan"; //nama database
    private static final String USER = "root"; //username MySQL
    private static final String PASSWORD = "1234"; //password MySQL 

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi ke database berhasil!");
            return conn;
        } 
         catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
            return null;
       }
    }    
}
