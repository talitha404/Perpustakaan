package perpustakaan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class daoDetailPinjam {
    private Connection conn;

    public daoDetailPinjam(Connection conn) {
        this.conn = conn;
    }

    public void insertDetailPinjam(cDetailPinjam dp) throws SQLException {
        String sql = "INSERT INTO detail_pinjam (id_pinjam, id_inventori, jumlah) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dp.getIdPinjam());
            stmt.setInt(2, dp.getIdInventori());
            stmt.setInt(3, dp.getJumlah());
            stmt.executeUpdate();
        }
    }

    public List<cDetailPinjam> getAllDetailPinjam() throws SQLException {
        List<cDetailPinjam> list = new ArrayList<>();
        String sql = "SELECT * FROM detail_pinjam";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new cDetailPinjam(
                    rs.getInt("id_detail_pinjam"),
                    rs.getInt("id_pinjam"),
                    rs.getInt("id_inventori"),
                    rs.getInt("jumlah")
                ));
            }
        }
        return list;
    }
}