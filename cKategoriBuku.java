package perpustakaan;
public class cKategoriBuku {
    private int idKategori;
    private String namaKategori;

    public cKategoriBuku() {}

    public cKategoriBuku(int idKategori, String namaKategori) {
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
    }

    public int getIdKategori() { return idKategori; }
    public void setIdKategori(int idKategori) { this.idKategori = idKategori; }

    public String getNamaKategori() { return namaKategori; }
    public void setNamaKategori(String namaKategori) { this.namaKategori = namaKategori; }
}