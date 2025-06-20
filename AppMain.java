package perpustakaan;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class AppMain {
    public static void main(String[] args) {
        Connection conn = DBConnection.connect();
        if (conn != null) {
            System.out.println("Koneksi ke database berhasil!");
        } else {
            System.out.println("Koneksi ke database gagal!");
            return; // Keluar dari aplikasi jika koneksi gagal
        }
        Scanner scanner = new Scanner(System.in);
        int pilihan;
        do {
            System.out.println("\n=== Menu Perpustakaan Digital ===");
            System.out.println("1. Login sebagai Pengunjung Lama");
            System.out.println("2. Daftar sebagai Pengunjung Baru");
            System.out.println("3. Login sebagai Admin");
            System.out.println("4. Keluar");
            System.out.print("Masukkan pilihan: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();
            daoUser userDao = new daoUser(conn);
            cUser user = null;
            
            switch (pilihan) {
                case 1:
                    System.out.print("Masukkan ID atau email: ");
                    String inputUser = scanner.nextLine();
                    try {
                        user = userDao.findUser(inputUser);
                    } catch (SQLException e) {
                        System.out.println("Terjadi kesalahan saat mengakses database: " + e.getMessage());
                    }
                    if (user != null) {
                        menuPengunjung(scanner, conn);
                    } else {
                        System.out.println("ID / email tidak valid, coba lagi.");
                    }
                    break;
                case 2:
                    daftarPengunjung(scanner, conn);
                    break;
                case 3:
                    System.out.print("Masukkan ID: ");
                    int idUser = scanner.nextInt();
                    scanner.nextLine(); // Konsumsi newline
                    System.out.print("Masukkan Password: ");
                    String password = scanner.nextLine();
                    try {
                        user = userDao.findUser(String.valueOf(idUser));
                    } catch (SQLException e) {
                        System.out.println("Terjadi kesalahan saat mengakses database: " + e.getMessage());
                    }

                    if (user != null && user.getRole().equalsIgnoreCase("admin") && user.getPassword() != null && user.getPassword().equals(password)) {
                        menuAdmin(scanner, conn);
                    } else {
                        System.out.println("ID atau password tidak valid, coba lagi.");
                    }
                    break;
                case 4:
                    System.out.println("Terima kasih telah menggunakan sistem perpustakaan!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid, coba lagi.");
            }
        } while (pilihan != 4);
        // Tutup koneksi database setelah aplikasi selesai
        try {
            conn.close();
            System.out.println("Koneksi database ditutup.");
        } catch (Exception e) {
            System.out.println("Gagal menutup koneksi: " + e.getMessage());
        }

        scanner.close();
    }

    public static void menuPengunjung(Scanner scanner, Connection conn) {
        int pilihan;
        daoUser userDAO = new daoUser(conn);
        daoBuku bukuDAO = new daoBuku(conn);
        cUser user;
        cBuku buku;
        do {
            System.out.println("\n=== Menu Pengunjung Lama ===");
            System.out.println("1. Pinjam Buku");
            System.out.println("2. Pengembalian Buku");
            System.out.println("3. Laporan Buku Hilang");
            System.out.println("4. Cari Buku");
            System.out.println("5. Lihat Buku Tersedia");
            System.out.println("6. Kembali ke Menu Utama");

            System.out.print("Masukkan pilihan: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    System.out.print("Masukkan ID buku yang ingin dipinjam: ");
                    int idBuku1 = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Apakah ingin meminjam buku lagi? (y/n): ");
                    String pilihanTambahan = scanner.nextLine();
                    int idBuku2 = -1;
                    if (pilihanTambahan.equalsIgnoreCase("y")) {
                        System.out.print("Masukkan ID buku kedua yang ingin dipinjam: ");
                        idBuku2 = scanner.nextInt();
                        scanner.nextLine();
                    }
                    try {
                        // Tentukan tanggal pinjam dan jatuh tempo (7 hari dari tanggal pinjam)
                        java.sql.Date tanggalPinjam = new java.sql.Date(System.currentTimeMillis());
                        java.sql.Date tanggalJatuhTempo = java.sql.Date.valueOf(
                            tanggalPinjam.toLocalDate().plusDays(7)
                        );
                        System.out.print("Masukkan ID User: ");
                        int idUser = scanner.nextInt();
                        scanner.nextLine();
                        String sqlPinjam = "INSERT INTO pinjam (id_user, tanggal_pinjam, tanggal_jatuh_tempo) VALUES (?, ?, ?)";
                        PreparedStatement stmtPinjam = conn.prepareStatement(sqlPinjam, Statement.RETURN_GENERATED_KEYS);
                        stmtPinjam.setInt(1, idUser);
                        stmtPinjam.setDate(2, tanggalPinjam);
                        stmtPinjam.setDate(3, tanggalJatuhTempo);
                        stmtPinjam.executeUpdate();
                        ResultSet rs = stmtPinjam.getGeneratedKeys();
                        int idPinjam = 0;
                        if (rs.next()) {
                            idPinjam = rs.getInt(1);
                        }
                        // Insert detail peminjaman untuk buku pertama
                        String sqlDetailPinjam = "INSERT INTO detail_pinjam (id_pinjam, id_inventori, jumlah) VALUES (?, ?, 1)";
                        PreparedStatement stmtDetailPinjam1 = conn.prepareStatement(sqlDetailPinjam);
                        stmtDetailPinjam1.setInt(1, idPinjam);
                        stmtDetailPinjam1.setInt(2, idBuku1);
                        stmtDetailPinjam1.executeUpdate();
                        // Jika ada buku kedua, tambahkan ke detail peminjaman
                        if (idBuku2 != -1) {
                            PreparedStatement stmtDetailPinjam2 = conn.prepareStatement(sqlDetailPinjam);
                            stmtDetailPinjam2.setInt(1, idPinjam);
                            stmtDetailPinjam2.setInt(2, idBuku2);
                            stmtDetailPinjam2.executeUpdate();
                        }
                        // Gunakan variabel `user` yang sudah dideklarasikan di awal
                        user = userDAO.findUser(String.valueOf(idUser));
                        System.out.println("\n=== Detail Peminjaman ===");
                        System.out.println("Nama Anggota: " + user.getNama());
                        System.out.println("ID Anggota  : " + user.getIdUser());
                        // Gunakan variabel `buku` yang sudah dideklarasikan di awal
                        List<cBuku> bukuList = new ArrayList<>();
                        bukuList.add(bukuDAO.findBukuById(idBuku1));
                        if (idBuku2 != -1) bukuList.add(bukuDAO.findBukuById(idBuku2));
                        for (cBuku bukuItem : bukuList) {
                            System.out.println("Nama Buku   : " + bukuItem.getJudul());
                        }
                        System.out.println("Tanggal Pinjam     : " + tanggalPinjam);
                        System.out.println("Tanggal Jatuh Tempo: " + tanggalJatuhTempo);
                        System.out.println("ID Peminjaman Buku: " + idPinjam + "(Harap ingat baik-baik id Peminjaman Buku!)");
                    } catch (SQLException e) {
                        System.out.println("Terjadi kesalahan dalam peminjaman: " + e.getMessage());
                    }
                    break;
                    
                case 2:
                    System.out.print("Masukkan ID buku yang ingin dikembalikan: ");
                    int idBukuKembali = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Masukkan ID pinjaman terkait: ");
                    int idPinjam = scanner.nextInt();
                    scanner.nextLine();
                    java.sql.Date tanggalKembali = new java.sql.Date(System.currentTimeMillis());
                    try {
                        // Ambil tanggal jatuh tempo dari database
                        String sqlJatuhTempo = "SELECT tanggal_jatuh_tempo FROM pinjam WHERE id_pinjam = ?";
                        PreparedStatement stmtJatuhTempo = conn.prepareStatement(sqlJatuhTempo);
                        stmtJatuhTempo.setInt(1, idPinjam);
                        ResultSet rsJatuhTempo = stmtJatuhTempo.executeQuery();
                        java.sql.Date tanggalJatuhTempo = null;
                        if (rsJatuhTempo.next()) {
                            tanggalJatuhTempo = rsJatuhTempo.getDate("tanggal_jatuh_tempo");
                        }
                        // Hitung denda jika terlambat
                        int jumlahDenda = 0;
                        if (tanggalKembali.after(tanggalJatuhTempo)) {
                            long selisihHari = (tanggalKembali.getTime() - tanggalJatuhTempo.getTime()) / (1000 * 60 * 60 * 24);
                            jumlahDenda = (int) selisihHari * 1000; // Rp 1000 per hari keterlambatan
                        }
                        // Masukkan data pengembalian buku ke database
                        String sqlPengembalian = "INSERT INTO detail_pengembalian (id_pinjam, id_inventori, jumlah, tanggal_kembali) VALUES (?, ?, 1, ?)";
                        PreparedStatement stmtPengembalian = conn.prepareStatement(sqlPengembalian);
                        stmtPengembalian.setInt(1, idPinjam);
                        stmtPengembalian.setInt(2, idBukuKembali);
                        stmtPengembalian.setDate(3, tanggalKembali);
                        stmtPengembalian.executeUpdate();
                        // Jalankan prosedur denda setelah pengembalian
                        daoDenda dendaDAO = new daoDenda(conn);
                        try {
                            dendaDAO.jalankanProsedurHitungDenda();
                            System.out.println("Denda berhasil dihitung (via prosedur).");
                        } catch (SQLException e) {
                            System.out.println("Gagal menjalankan prosedur denda: " + e.getMessage());
                        }
                        // Masukkan denda jika ada
                        if (jumlahDenda > 0) {
                            String sqlDenda = "INSERT INTO denda (id_detail_pengembalian, jumlah_denda) VALUES ((SELECT MAX(id_detail_pengembalian) FROM detail_pengembalian), ?)";
                            PreparedStatement stmtDenda = conn.prepareStatement(sqlDenda);
                            stmtDenda.setInt(1, jumlahDenda);
                            stmtDenda.executeUpdate();
                        }
                        // Ambil informasi peminjam
                        String sqlUser = "SELECT u.nama, u.id_user, b.judul FROM user u JOIN pinjam p ON u.id_user = p.id_user JOIN buku b ON b.id_judul = ? WHERE p.id_pinjam = ?";
                        PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
                        stmtUser.setInt(1, idBukuKembali);
                        stmtUser.setInt(2, idPinjam);
                        ResultSet rsUser = stmtUser.executeQuery();
                        // Tampilkan detail pengembalian
                        if (rsUser.next()) {
                            System.out.println("\n=== Detail Pengembalian ===");
                            System.out.println("Nama Anggota: " + rsUser.getString("nama"));
                            System.out.println("ID Anggota  : " + rsUser.getInt("id_user"));
                            System.out.println("Nama Buku   : " + rsUser.getString("judul"));
                            System.out.println("Tanggal Kembali    : " + tanggalKembali);
                            System.out.println("Total Denda: Rp " + jumlahDenda);
                        }
                    } catch (SQLException e) {
                        System.out.println("Terjadi kesalahan dalam pengembalian: " + e.getMessage());
                    }
                    break;
                    
                case 3:
                    System.out.print("Masukkan ID buku yang hilang: ");
                    int idBukuHilang = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Masukkan ID pinjaman terkait: ");
                    int idPinjamHilang = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        // Cari data peminjam yang terkait dengan buku tersebut
                        String sqlPeminjam = "SELECT u.nama, u.id_user, b.judul, p.tanggal_jatuh_tempo " +
                                             "FROM user u " +
                                             "JOIN pinjam p ON u.id_user = p.id_user " +
                                             "JOIN buku b ON b.id_judul = ? " +
                                             "WHERE p.id_pinjam = ?";
                        PreparedStatement stmtPeminjam = conn.prepareStatement(sqlPeminjam);
                        stmtPeminjam.setInt(1, idBukuHilang);
                        stmtPeminjam.setInt(2, idPinjamHilang);
                        ResultSet rsPeminjam = stmtPeminjam.executeQuery();
                        if (rsPeminjam.next()) {
                            String namaPeminjam = rsPeminjam.getString("nama");
                            int idUser = rsPeminjam.getInt("id_user");
                            String judulBuku = rsPeminjam.getString("judul");
                            System.out.println("\n=== Laporan Buku Hilang ===");
                            System.out.println("Nama Peminjam  : " + namaPeminjam);
                            System.out.println("ID Peminjam    : " + idUser);
                            System.out.println("Judul Buku     : " + judulBuku);
                            System.out.println("Buku ini dilaporkan hilang.");
                            // Hitung denda buku hilang
                            int hargaDenda = 50000; // Misalnya Rp 50.000 per buku hilang
                            System.out.println("Jumlah Denda: Rp " + hargaDenda);
                            // Update stok buku (kurangi jumlah karena hilang)
                            String sqlUpdateStok = "UPDATE inventori SET stok = stok - 1 WHERE id_judul = ?";
                            PreparedStatement stmtUpdateStok = conn.prepareStatement(sqlUpdateStok);
                            stmtUpdateStok.setInt(1, idBukuHilang);
                            stmtUpdateStok.executeUpdate();
                            // Simpan data denda buku hilang
                            String sqlDendaHilang = "INSERT INTO denda (id_detail_pengembalian, jumlah_denda) VALUES ((SELECT MAX(id_detail_pengembalian) FROM detail_pengembalian WHERE id_pinjam = ?), ?)";
                            PreparedStatement stmtDendaHilang = conn.prepareStatement(sqlDendaHilang);
                            stmtDendaHilang.setInt(1, idPinjamHilang);
                            stmtDendaHilang.setInt(2, hargaDenda);
                            stmtDendaHilang.executeUpdate();
                            System.out.println("Denda telah tercatat dalam sistem.");

                        } else {
                            System.out.println("Data peminjam tidak ditemukan.");
                        }

                    } catch (SQLException e) {
                        System.out.println("Terjadi kesalahan dalam laporan buku hilang: " + e.getMessage());
                    }
                    break;
                case 4:
                     System.out.println("\n=== Cari Buku ===");
                            System.out.print("Masukkan kata kunci pencarian: ");
                            String keyword = scanner.nextLine();
                            try {
                                List<cBuku> hasilPencarian = bukuDAO.searchBuku(keyword);
                                if (hasilPencarian.isEmpty()) {
                                    System.out.println("Tidak ada buku yang cocok dengan kata kunci: " + keyword);
                                } else {
                                    System.out.println("\n?Hasil Pencarian:");
                                    System.out.println("ID\tJudul\tPenulis\tPenerbit\tTahun\tKategori");
                                    System.out.println("------------------------------------------------------------");
                                    for (cBuku b : hasilPencarian) {
                                        System.out.println(b.getIdJudul() + "\t" + b.getJudul() + "\t" + b.getPenulis() + "\t" + b.getPenerbit() + "\t" + b.getTahunTerbit() + "\t" + b.getIdKategori());
                                    }
                                }
                            } catch (SQLException e) {
                                System.out.println("Gagal mencari buku: " + e.getMessage());
                            }
                    break;
                case 5:
                    System.out.println("\n=== Daftar Buku Tersedia ===");
                    String sql = "SELECT b.id_judul, b.judul, b.penulis, b.penerbit, b.tahun_terbit, " +
                                 "k.nama_kategori, i.stok, i.lokasi_rak " +
                                 "FROM buku b " +
                                 "JOIN kategori_buku k ON b.id_kategori = k.id_kategori " +
                                 "JOIN inventori i ON b.id_judul = i.id_judul " +
                                 "WHERE i.stok > 0";

                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(sql)) {

                        System.out.printf("%-5s %-25s %-15s %-15s %-6s %-15s %-6s %-10s\n",
                            "ID", "Judul", "Penulis", "Penerbit", "Tahun", "Kategori", "Stok", "Rak");
                        System.out.println("-------------------------------------------------------------------------------");

                        while (rs.next()) {
                            System.out.printf("%-5d %-25s %-15s %-15s %-6d %-15s %-6d %-10s\n",
                                rs.getInt("id_judul"),
                                rs.getString("judul"),
                                rs.getString("penulis"),
                                rs.getString("penerbit"),
                                rs.getInt("tahun_terbit"),
                                rs.getString("nama_kategori"),
                                rs.getInt("stok"),
                                rs.getString("lokasi_rak")
                            );
                        }

                    } catch (SQLException e) {
                        System.out.println("Gagal mengambil data buku tersedia: " + e.getMessage());
                    }
                break;
                case 6:
                    System.out.println("Kembali ke Menu Utama...");
                    break;
                default:
                    System.out.println("Pilihan tidak valid, coba lagi.");
            }
        } while (pilihan != 6);
    }

    public static void daftarPengunjung(Scanner scanner, Connection conn) {
        System.out.println("\n=== Pendaftaran Pengunjung Baru ===");

        // Input dari pengguna
        System.out.print("Masukkan Nama: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan Email: ");
        String email = scanner.nextLine();
        System.out.print("Masukkan Telepon: ");
        String telepon = scanner.nextLine();
        System.out.print("Masukkan Alamat: ");
        String alamat = scanner.nextLine();

        // Tetapkan role sebagai "member" dan password sebagai NULL
        String role = "member";
        String password = null;

        // Gunakan objek `cUser` untuk menyimpan data
        cUser user = new cUser(0, nama, role, password, email, telepon, alamat);

        try {
            // Gunakan DAO untuk menyimpan data ke database
            daoUser userDAO = new daoUser(conn);
            int generatedId = userDAO.insertUser(user);
            System.out.println("Akun member berhasil dibuat untuk " + nama + " dengan ID " + generatedId);
        } catch (SQLException e) {
            System.out.println("Gagal mendaftarkan pengunjung: " + e.getMessage());
        }
    }

    public static void menuAdmin(Scanner scanner, Connection conn) {
    int pilihan;
    daoUser userDAO = new daoUser(conn);
    daoBuku bukuDAO = new daoBuku(conn);
    cUser user;
    cBuku buku;
    
    do {
        System.out.println("\n=== Menu Admin ===");
        System.out.println("1. Sistem Buku");
        System.out.println("2. Sistem User");
        System.out.println("3. Sistem Peminjaman & Pengembalian");
        System.out.println("4. Kembali ke Menu Utama");
        System.out.print("Masukkan pilihan: ");
        pilihan = scanner.nextInt();
        scanner.nextLine(); // Konsumsi newline

        switch (pilihan) {
            case 1: // SISTEM BUKU
                int subMenuBuku;
                do {
                    System.out.println("\n=== Sistem Buku ===");
                    System.out.println("1. Tambah Buku");
                    System.out.println("2. Hapus Buku");
                    System.out.println("3. Update Buku");
                    System.out.println("4. Cari Buku");
                    System.out.println("5. Lihat Buku");
                    System.out.println("6. Kembali ke Menu Admin");
                    System.out.print("Masukkan pilihan: ");
                    subMenuBuku = scanner.nextInt();
                    scanner.nextLine();

                    switch (subMenuBuku) {
                       case 1: // Tambah Buku
                            System.out.println("\n=== Tambah Buku ===");
                            System.out.print("Masukkan Judul: ");
                            String judul = scanner.nextLine();
                            System.out.print("Masukkan Penulis: ");
                            String penulis = scanner.nextLine();
                            System.out.print("Masukkan Penerbit: ");
                            String penerbit = scanner.nextLine();
                            System.out.print("Masukkan Tahun Terbit: ");
                            int tahunTerbit = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Masukkan ID Kategori: ");
                            int idKategori = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Masukkan Stok Awal: ");
                            int stok = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Masukkan Lokasi Rak: ");
                            String lokasiRak = scanner.nextLine();

                            // Buat objek buku
                            buku = new cBuku(0, judul, penulis, penerbit, tahunTerbit, idKategori);

                            try {
                                // Simpan buku ke database dan ambil ID-nya
                                int idJudulBaru = bukuDAO.insertBuku(buku);

                                // Buat entri inventori
                                cInventori inv = new cInventori(0, idJudulBaru, stok, stok, lokasiRak);
                                daoInventori invDAO = new daoInventori(conn);
                                invDAO.insertInventori(inv);

                                System.out.println("Buku berhasil ditambahkan: " + judul + " (Stok awal: " + stok + ")");
                            } catch (SQLException e) {
                                System.out.println("Gagal menambahkan buku: " + e.getMessage());
                            }
                            break;

                        case 2: // Hapus Buku
                        System.out.println("\n=== Hapus Buku ===");
                        System.out.print("Masukkan ID Buku yang ingin dihapus: ");
                        int idJudulHapus = scanner.nextInt();
                        scanner.nextLine();
                        try {
                            daoInventori invDAO = new daoInventori(conn);
                            invDAO.deleteByJudul(idJudulHapus); // hapus dulu dari inventori
                            bukuDAO.deleteBuku(idJudulHapus);   // baru hapus buku
                        } catch (SQLException e) {
                            System.out.println("Gagal menghapus buku: " + e.getMessage());
                        }
                        break;

                        case 3: // Update Buku
                            System.out.println("\n=== Update Buku ===");
                            System.out.print("Masukkan ID Buku yang ingin diperbarui: ");
                            int idJudulUpdate = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Masukkan Judul Baru: ");
                            judul = scanner.nextLine();
                            System.out.print("Masukkan Penulis Baru: ");
                            penulis = scanner.nextLine();
                            System.out.print("Masukkan Penerbit Baru: ");
                            penerbit = scanner.nextLine();
                            System.out.print("Masukkan Tahun Terbit Baru: ");
                            tahunTerbit = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Masukkan ID Kategori Baru: ");
                            idKategori = scanner.nextInt();
                            scanner.nextLine();

                            try {
                                bukuDAO.updateBuku(idJudulUpdate, judul, penulis, penerbit, tahunTerbit, idKategori);
                                System.out.println("Buku berhasil diperbarui.");
                            } catch (SQLException e) {
                                System.out.println("Gagal memperbarui buku: " + e.getMessage());
                            }
                            break;

                        case 4: // Cari Buku
                            System.out.println("\n=== Cari Buku ===");
                            System.out.print("Masukkan kata kunci pencarian: ");
                            String keyword = scanner.nextLine();
                            try {
                                List<cBuku> hasilPencarian = bukuDAO.searchBuku(keyword);
                                if (hasilPencarian.isEmpty()) {
                                    System.out.println("Tidak ada buku yang cocok dengan kata kunci: " + keyword);
                                } else {
                                    System.out.println("\n?Hasil Pencarian:");
                                    System.out.println("ID\tJudul\tPenulis\tPenerbit\tTahun\tKategori");
                                    System.out.println("------------------------------------------------------------");
                                    for (cBuku b : hasilPencarian) {
                                        System.out.println(b.getIdJudul() + "\t" + b.getJudul() + "\t" + b.getPenulis() + "\t" + b.getPenerbit() + "\t" + b.getTahunTerbit() + "\t" + b.getIdKategori());
                                    }
                                }
                            } catch (SQLException e) {
                                System.out.println("Gagal mencari buku: " + e.getMessage());
                            }
                            break;

                        case 5: // Lihat Buku
                        System.out.println("\n=== Daftar Buku dengan Stok ===");
                        String sql = "SELECT b.id_judul, b.judul, b.penulis, i.stok " +
                                     "FROM buku b " +
                                     "JOIN inventori i ON b.id_judul = i.id_judul";

                        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                            System.out.printf("%-5s %-30s %-20s %-6s\n", "ID", "Judul", "Penulis", "Stok");
                            System.out.println("------------------------------------------------------");

                            while (rs.next()) {
                                System.out.printf("%-5d %-30s %-20s %-6d\n",
                                    rs.getInt("id_judul"),
                                    rs.getString("judul"),
                                    rs.getString("penulis"),
                                    rs.getInt("stok"));
                            }
                        } catch (SQLException e) {
                            System.out.println("Gagal mengambil data buku: " + e.getMessage());
                        }
                        break;

                        case 6:
                            System.out.println("Kembali ke Menu Admin...");
                            break;
                        default:
                            System.out.println("Pilihan tidak valid, coba lagi.");
                    }
                } while (subMenuBuku!= 6);
                break;

            case 2: // SISTEM USER
                int subMenuUser;
                do {
                    System.out.println("\n=== Sistem User ===");
                    System.out.println("1. Tambah User");
                    System.out.println("2. Hapus User");
                    System.out.println("3. Update User");
                    System.out.println("4. Cari User");
                    System.out.println("5. Lihat User");
                    System.out.println("6. Kembali ke Menu Admin");
                    System.out.print("Masukkan pilihan: ");
                    subMenuUser = scanner.nextInt();
                    scanner.nextLine();

                    switch (subMenuUser) {
                        case 1: // Tambah User
                            System.out.println("\n=== Tambah User Baru ===");
                            System.out.print("Masukkan Nama: ");
                            String nama = scanner.nextLine();
                            System.out.print("Masukkan Role (Admin/Member): ");
                            String role = scanner.nextLine();
                            System.out.print("Masukkan Password: ");
                            String password = scanner.nextLine();
                            System.out.print("Masukkan Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Masukkan Telepon: ");
                            String telepon = scanner.nextLine();
                            System.out.print("Masukkan Alamat: ");
                            String alamat = scanner.nextLine();

                            user = new cUser(0, nama, role, password, email, telepon, alamat);
                            try {
                                userDAO.insertUser(user);
                                System.out.println("User berhasil ditambahkan: " + nama);
                            } catch (SQLException e) {
                                System.out.println("Gagal menambahkan user: " + e.getMessage());
                            }
                            break;

                        case 2: // Hapus User
                            System.out.println("\n=== Hapus User ===");
                            System.out.print("Masukkan ID atau Nama User yang ingin dihapus: ");
                            String inputHapus = scanner.nextLine();
                            try {
                                userDAO.deleteUser(inputHapus);
                            } catch (SQLException e) {
                                System.out.println("Gagal menghapus user: " + e.getMessage());
                            }
                            break;

                        case 3: // Update User
                            System.out.println("\n=== Update User ===");
                            System.out.print("Masukkan ID User yang ingin diperbarui: ");
                            int idUserUpdate = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Masukkan Nama Baru: ");
                            nama = scanner.nextLine();
                            System.out.print("Masukkan Role Baru (Admin/Member): ");
                            role = scanner.nextLine();
                            System.out.print("Masukkan Password Baru: ");
                            password = scanner.nextLine();
                            System.out.print("Masukkan Email Baru: ");
                            email = scanner.nextLine();
                            System.out.print("Masukkan Telepon Baru: ");
                            telepon = scanner.nextLine();
                            System.out.print("Masukkan Alamat Baru: ");
                            alamat = scanner.nextLine();

                            try {
                                userDAO.updateUser(idUserUpdate, nama, role, password, email, telepon, alamat);
                                System.out.println("User berhasil diperbarui.");
                            } catch (SQLException e) {
                                System.out.println("Gagal memperbarui user: " + e.getMessage());
                            }
                            break;

                        case 4: // Cari User
                            System.out.println("\n=== Cari User ===");
                            System.out.print("Masukkan ID, Nama, atau Email User yang ingin dicari: ");
                            String inputCari = scanner.nextLine();
                            try {
                                cUser foundUser = userDAO.findUser(inputCari);
                                if (foundUser != null) {
                                    System.out.println("\nUser ditemukan:");
                                    System.out.println("ID : " + foundUser.getIdUser());
                                    System.out.println("Nama : " + foundUser.getNama());
                                    System.out.println("Role : " + foundUser.getRole());
                                    System.out.println("Email : " + foundUser.getEmail());
                                    System.out.println("Telepon : " + foundUser.getTelepon());
                                    System.out.println("Alamat : " + foundUser.getAlamat());
                                } else {
                                    System.out.println("User tidak ditemukan.");
                                }
                            } catch (SQLException e) {
                                System.out.println("Gagal mencari user: " + e.getMessage());
                            }
                            break;

                        case 5: // Lihat User
                            System.out.println("\n=== Daftar User ===");
                            try {
                                List<cUser> users = userDAO.getAllUsers();
                                if (users.isEmpty()) {
                                    System.out.println("Tidak ada user yang terdaftar.");
                                } else {
                                    System.out.println("ID\tNama\tRole\tEmail\tTelepon\tAlamat");
                                    System.out.println("------------------------------------------------------------");
                                    for (cUser u : users) {
                                        System.out.println(u.getIdUser() + "\t" + u.getNama() + "\t" + u.getRole() + "\t"
                                                + u.getEmail() + "\t" + u.getTelepon() + "\t" + u.getAlamat());
                                    }
                                }
                            } catch (SQLException e) {
                                System.out.println("Gagal mengambil daftar user: " + e.getMessage());
                            }
                            break;

                        case 6:
                            System.out.println("Kembali ke Menu Admin...");
                            break;

                        default:
                            System.out.println("Pilihan tidak valid, coba lagi.");
                    }
                } while (subMenuUser != 6);
                break;

            case 3: // Sistem Peminjaman & Pengembalian
                System.out.println("\n=== Laporan Peminjaman & Pengembalian ===");
                String callLaporan = "CALL laporan_peminjaman_pengembalian()";

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(callLaporan)) {

                    System.out.printf("%-5s %-5s %-15s %-7s %-25s %-12s %-12s %-12s %-10s\n", 
                        "Pinj", "User", "Nama", "Judul", "Buku", "Pinjam", "Jatuh", "Kembali", "Denda");
                    System.out.println("-----------------------------------------------------------------------------------------------------");

                    while (rs.next()) {
                        int idPinjam = rs.getInt("id_pinjam");
                        int idUser = rs.getInt("id_user");
                        String namaUser = rs.getString("nama_user");
                        int idJudul = rs.getInt("id_judul");
                        String judul = rs.getString("judul");
                        Date tglPinjam = rs.getDate("tanggal_pinjam");
                        Date tglJatuhTempo = rs.getDate("tanggal_jatuh_tempo");
                        Date tglKembali = rs.getDate("tanggal_kembali");
                        double denda = rs.getDouble("jumlah_denda");

                        System.out.printf("%-5d %-5d %-15s %-7d %-25s %-12s %-12s %-12s Rp%-9.2f\n",
                            idPinjam, idUser, namaUser, idJudul, judul, 
                            String.valueOf(tglPinjam), 
                            String.valueOf(tglJatuhTempo), 
                            String.valueOf(tglKembali), 
                            denda);
                    }

                } catch (SQLException e) {
                    System.out.println("Gagal mengambil data laporan: " + e.getMessage());
                }
                break;

            case 4: // Kembali ke Menu Utama
                System.out.println("Kembali ke Menu Utama...");
                break;

            default:
                System.out.println("Pilihan tidak valid.");
        }
    } while (pilihan != 4);
}

}



