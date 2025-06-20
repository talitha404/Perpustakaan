package perpustakaan;

public class cInventori {
    private int idInventori;
    private int idJudul;
    private int jumlahAwal;
    private int stok;
    private String lokasiRak;

    public cInventori() {}

    public cInventori(int idInventori, int idJudul, int jumlahAwal, int stok, String lokasiRak) {
        this.idInventori = idInventori;
        this.idJudul = idJudul;
        this.jumlahAwal = jumlahAwal;
        this.stok = stok;
        this.lokasiRak = lokasiRak;
    }

    public int getIdInventori() { return idInventori; }
    public void setIdInventori(int idInventori) { this.idInventori = idInventori; }

    public int getIdJudul() { return idJudul; }
    public void setIdJudul(int idJudul) { this.idJudul = idJudul; }

    public int getJumlahAwal() { return jumlahAwal; }
    public void setJumlahAwal(int jumlahAwal) { this.jumlahAwal = jumlahAwal; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public String getLokasiRak() { return lokasiRak; }
    public void setLokasiRak(String lokasiRak) { this.lokasiRak = lokasiRak; }
}