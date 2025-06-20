package perpustakaan;
import java.sql.*;
import java.util.*;

public class daoDetailPengembalian {
    private Connection conn;

    public daoDetailPengembalian(Connection conn) {
        this.conn = conn;
    }

    public void insertDetailPengembalian(cDetailPengembalian dp) throws SQLException {
        String sql = "INSERT INTO detail_pengembalian (id_pinjam, id_inventori, jumlah, tanggal_kembali) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dp.getIdPinjam());
            stmt.setInt(2, dp.getIdInventori());
            stmt.setInt(3, dp.getJumlah());
            stmt.setDate(4, dp.getTanggalKembali());
            stmt.executeUpdate();
        }
    }

    public List<cDetailPengembalian> getAllDetailPengembalian() throws SQLException {
        List<cDetailPengembalian> list = new ArrayList<>();
        String sql = "SELECT * FROM detail_pengembalian";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new cDetailPengembalian(
                    rs.getInt("id_detail_pengembalian"),
                    rs.getInt("id_pinjam"),
                    rs.getInt("id_inventori"),
                    rs.getInt("jumlah"),
                    rs.getDate("tanggal_kembali")
                ));
            }
        }
        return list;
    }
}