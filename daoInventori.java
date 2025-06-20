package perpustakaan;
import java.sql.*;
import java.util.*;


public class daoInventori {
    private Connection conn;

    public daoInventori(Connection conn) {
        this.conn = conn;
    }

    public void insertInventori(cInventori inv) throws SQLException {
        String sql = "INSERT INTO inventori (id_judul, jumlah_awal, stok, lokasi_rak) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inv.getIdJudul());
            stmt.setInt(2, inv.getJumlahAwal());
            stmt.setInt(3, inv.getJumlahAwal()); // stok awal = jumlah awal
            stmt.setString(4, inv.getLokasiRak());
            stmt.executeUpdate();
        }
    }

    public List<cInventori> getAllInventori() throws SQLException {
        List<cInventori> list = new ArrayList<>();
        String sql = "SELECT * FROM inventori";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new cInventori(
                rs.getInt("id_inventori"),
                rs.getInt("id_judul"),
                rs.getInt("jumlah_awal"),
                rs.getInt("stok"),
                rs.getString("lokasi_rak")
            ));
            }
        }
        return list;
    }
    
    public void deleteByJudul(int idJudul) throws SQLException {
    String sql = "DELETE FROM inventori WHERE id_judul = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idJudul);
        stmt.executeUpdate();
    }
}
}