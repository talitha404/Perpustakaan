package perpustakaan;
import java.sql.*;
import java.util.*;

public class daoKategoriBuku {
    private Connection conn;

    public daoKategoriBuku(Connection conn) {
        this.conn = conn;
    }

    public void insertKategori(cKategoriBuku kat) throws SQLException {
        String sql = "INSERT INTO kategori_buku (id_kategori, nama_kategori) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, kat.getIdKategori());
            stmt.setString(2, kat.getNamaKategori());
            stmt.executeUpdate();
        }
    }

    public List<cKategoriBuku> getAllKategori() throws SQLException {
        List<cKategoriBuku> list = new ArrayList<>();
        String sql = "SELECT * FROM kategori_buku";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new cKategoriBuku(
                    rs.getInt("id_kategori"),
                    rs.getString("nama_kategori")
                ));
            }
        }
        return list;
    }
}