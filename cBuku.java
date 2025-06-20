package perpustakaan;
public class cBuku {
    private int idJudul;
    private String judul;
    private String penulis;
    private String penerbit;
    private int tahunTerbit;
    private int idKategori;

    // Konstruktor
    public cBuku() {}

    public cBuku(int idJudul, String judul, String penulis, String penerbit, int tahunTerbit, int idKategori) {
        this.idJudul = idJudul;
        this.judul = judul;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.idKategori = idKategori;
    }

    // Getter dan Setter
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

    public int getIdKategori() { return idKategori; }
    public void setIdKategori(int idKategori) { this.idKategori = idKategori; }
}
