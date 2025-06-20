package perpustakaan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class daoPinjam {
    private Connection conn;

    public daoPinjam(Connection conn) {
        this.conn = conn;
    }

    public void insertPinjam(cPinjam p) throws SQLException {
        String sql = "INSERT INTO pinjam (id_user, tanggal_pinjam, tanggal_jatuh_tempo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getIdUser());
            stmt.setDate(2, p.getTanggalPinjam());
            stmt.setDate(3, p.getTanggalJatuhTempo());
            stmt.executeUpdate();
        }
    }

    public List<cPinjam> getAllPinjam() throws SQLException {
        List<cPinjam> list = new ArrayList<>();
        String sql = "SELECT * FROM pinjam";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new cPinjam(
                    rs.getInt("id_pinjam"),
                    rs.getInt("id_user"),
                    rs.getDate("tanggal_pinjam"),
                    rs.getDate("tanggal_jatuh_tempo")
                ));
            }
        }
        return list;
    }
}