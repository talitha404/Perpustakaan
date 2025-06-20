package perpustakaan;

public class BukuTersediaView {
    private int idJudul;
    private String judul;
    private String penulis;
    private String penerbit;
    private int tahunTerbit;
    private String namaKategori;
    private int stok;
    private String lokasiRak;

    public BukuTersediaView(int idJudul, String judul, String penulis, String penerbit,
                            int tahunTerbit, String namaKategori, int stok, String lokasiRak) {
        this.idJudul = idJudul;
        this.judul = judul;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.namaKategori = namaKategori;
        this.stok = stok;
        this.lokasiRak = lokasiRak;
    }

    // Getters dan Setters
    public int getIdJudul() { return idJudul; }
    public void setIdJudul(int idJudul) { this.idJudul = idJudul; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getPenulis() { return penulis; }
    public void setPenulis(String penulis) { this.penulis = penulis; }

    public String getPenerbit() { return penerbit; }
    public void setPenerbit(String penerbit) { this.penerbit = penerbit; }

    public int getTahunTerbit() { return tahunTerbit; }
    public void setTahunTerbit(int tahunTerbit) { this.tahunTerbit = tahunTerbit; }

    public String getNamaKategori() { return namaKategori; }
    public void setNamaKategori(String namaKategori) { this.namaKategori = namaKategori; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public String getLokasiRak() { return lokasiRak; }
    public void setLokasiRak(String lokasiRak) { this.lokasiRak = lokasiRak; }
}
