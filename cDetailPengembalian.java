package perpustakaan;
import java.sql.Date;

public class cDetailPengembalian {
    private int idDetailPengembalian;
    private int idPinjam;
    private int idInventori;
    private int jumlah;
    private Date tanggalKembali;

    public cDetailPengembalian() {}

    public cDetailPengembalian(int idDetailPengembalian, int idPinjam, int idInventori, int jumlah, Date tanggalKembali) {
        this.idDetailPengembalian = idDetailPengembalian;
        this.idPinjam = idPinjam;
        this.idInventori = idInventori;
        this.jumlah = jumlah;
        this.tanggalKembali = tanggalKembali;
    }

    public int getIdDetailPengembalian() { return idDetailPengembalian; }
    public void setIdDetailPengembalian(int id) { this.idDetailPengembalian = id; }

    public int getIdPinjam() { return idPinjam; }
    public void setIdPinjam(int idPinjam) { this.idPinjam = idPinjam; }

    public int getIdInventori() { return idInventori; }
    public void setIdInventori(int idInventori) { this.idInventori = idInventori; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public Date getTanggalKembali() { return tanggalKembali; }
    public void setTanggalKembali(Date tanggalKembali) { this.tanggalKembali = tanggalKembali; }
}