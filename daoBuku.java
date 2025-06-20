package perpustakaan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class daoBuku {
    private Connection conn;

    public daoBuku(Connection conn) {
        this.conn = conn;
    }

    // Insert Buku ke Database
    public int insertBuku(cBuku buku) throws SQLException {
    String sql = "INSERT INTO buku (judul, penulis, penerbit, tahun_terbit, id_kategori) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, buku.getJudul());
        stmt.setString(2, buku.getPenulis());
        stmt.setString(3, buku.getPenerbit());
        stmt.setInt(4, buku.getTahunTerbit());
        stmt.setInt(5, buku.getIdKategori());
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
    }
    return -1;
    }

    // Delete Buku dari Database
    public void deleteBuku(int idJudul) throws SQLException {
        String sql = "DELETE FROM buku WHERE id_judul = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idJudul);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Buku dengan ID " + idJudul + " berhasil dihapus.");
            } else {
                System.out.println("Buku dengan ID " + idJudul + " tidak ditemukan.");
            }
        }
    }

    // Update Buku dalam Database
    public void updateBuku(int idJudul, String judul, String penulis, String penerbit, int tahunTerbit, int idKategori) throws SQLException {
        String sql = "UPDATE buku SET judul = ?, penulis = ?, penerbit = ?, tahun_terbit = ?, id_kategori = ? WHERE id_judul = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, judul);
            stmt.setString(2, penulis);
            stmt.setString(3, penerbit);
            stmt.setInt(4, tahunTerbit);
            stmt.setInt(5, idKategori);
            stmt.setInt(6, idJudul);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Buku dengan ID " + idJudul + " berhasil diperbarui.");
            } else {
                System.out.println("Buku dengan ID " + idJudul + " tidak ditemukan.");
            }
        }
    }

    // Search Buku berdasarkan Judul, Penulis, atau Penerbit
    public List<cBuku> searchBuku(String keyword) throws SQLException {
        List<cBuku> bukuList = new ArrayList<>();
        String sql = "SELECT * FROM buku WHERE judul LIKE ? OR penulis LIKE ? OR penerbit LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bukuList.add(new cBuku(
                    rs.getInt("id_judul"),
                    rs.getString("judul"),
                    rs.getString("penulis"),
                    rs.getString("penerbit"),
                    rs.getInt("tahun_terbit"),
                    rs.getInt("id_kategori")
                ));
            }
        }
        return bukuList;
    }

    // Get Semua Buku dari Database
    public List<cBuku> getAllBuku() throws SQLException {
        List<cBuku> listBuku = new ArrayList<>();
        String sql = "SELECT * FROM buku";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                listBuku.add(new cBuku(
                    rs.getInt("id_judul"),
                    rs.getString("judul"),
                    rs.getString("penulis"),
                    rs.getString("penerbit"),
                    rs.getInt("tahun_terbit"),
                    rs.getInt("id_kategori")
                ));
            }
        }
        return listBuku;
    }
    
    public cBuku findBukuById(int idJudul) throws SQLException {
    String sql = "SELECT * FROM buku WHERE id_judul = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idJudul);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new cBuku(
                rs.getInt("id_judul"),
                rs.getString("judul"),
                rs.getString("penulis"),
                rs.getString("penerbit"),
                rs.getInt("tahun_terbit"),
                rs.getInt("id_kategori")
            );
        }
    }
    return null; // Jika buku tidak ditemukan
}
   
}