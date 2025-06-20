package perpustakaan;
public class cDetailPinjam {
    private int idDetailPinjam;
    private int idPinjam;
    private int idInventori;
    private int jumlah;

    public cDetailPinjam() {}

    public cDetailPinjam(int idDetailPinjam, int idPinjam, int idInventori, int jumlah) {
        this.idDetailPinjam = idDetailPinjam;
        this.idPinjam = idPinjam;
        this.idInventori = idInventori;
        this.jumlah = jumlah;
    }

    public int getIdDetailPinjam() { return idDetailPinjam; }
    public void setIdDetailPinjam(int idDetailPinjam) { this.idDetailPinjam = idDetailPinjam; }

    public int getIdPinjam() { return idPinjam; }
    public void setIdPinjam(int idPinjam) { this.idPinjam = idPinjam; }

    public int getIdInventori() { return idInventori; }
    public void setIdInventori(int idInventori) { this.idInventori = idInventori; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
}