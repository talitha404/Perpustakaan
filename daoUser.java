package perpustakaan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class daoUser {
    private Connection conn;

    public daoUser(Connection conn) {
        this.conn = conn;
    }

    public int insertUser(cUser user) throws SQLException {
        String sql = "INSERT INTO user (nama, role, password, email, telepon, alamat) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getNama());
            stmt.setString(2, user.getRole());
            if (user.getPassword() == null) {
                stmt.setNull(3, Types.VARCHAR);
            } else {
                stmt.setString(3, user.getPassword());
            }
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getTelepon());
            stmt.setString(6, user.getAlamat());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // ID yang di-generate
            }
        }
        return -1; // gagal menyisipkan
    }

    public void deleteUser(String input) throws SQLException {
        String sql;

        // Tentukan apakah input berupa angka (ID) atau teks (Nama)
        if (input.matches("\\d+")) { 
            sql = "DELETE FROM user WHERE id_user = ?";
        } else {
            sql = "DELETE FROM user WHERE nama = ?";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, input);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User dengan " + (input.matches("\\d+") ? "ID " : "Nama ") + input + " berhasil dihapus.");
            } else {
                System.out.println("User dengan " + (input.matches("\\d+") ? "ID " : "Nama ") + input + " tidak ditemukan.");
            }
        }
    }
    
    public void updateUser(int idUser, String nama, String role, String password, String email, String telepon, String alamat) throws SQLException {
        String sql = "UPDATE user SET nama = ?, role = ?, password = ?, email = ?, telepon = ?, alamat = ? WHERE id_user = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nama);
            stmt.setString(2, role);
            stmt.setString(3, password);
            stmt.setString(4, email);
            stmt.setString(5, telepon);
            stmt.setString(6, alamat);
            stmt.setInt(7, idUser);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User dengan ID " + idUser + " berhasil diperbarui.");
            } else {
                System.out.println("User dengan ID " + idUser + " tidak ditemukan.");
            }
        }
    }
    
    public cUser findUser(String input) throws SQLException {
        String sql;

        // Tentukan apakah input berupa angka (ID) atau teks (Nama/Email)
        if (input.matches("\\d+")) {
            sql = "SELECT * FROM user WHERE id_user = ?";
        } else if (input.contains("@")) {
            sql = "SELECT * FROM user WHERE email = ?";
        } else {
            sql = "SELECT * FROM user WHERE nama = ?";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, input);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new cUser(
                    rs.getInt("id_user"),
                    rs.getString("nama"),
                    rs.getString("role"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("telepon"),
                    rs.getString("alamat")
                );
            } else {
                return null; // User tidak ditemukan
            }
        }
    }
    
    public List<cUser> getAllUsers() throws SQLException {
        List<cUser> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cUser user = new cUser(
                    rs.getInt("id_user"),
                    rs.getString("nama"),
                    rs.getString("role"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("telepon"),
                    rs.getString("alamat")
                );
                users.add(user);
            }
        }
        return users;
    }
}