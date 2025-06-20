package perpustakaan;
public class cDenda {
    private int idDenda;
    private int idDetailPengembalian;
    private double jumlahDenda;

    public cDenda() {}

    public cDenda(int idDenda, int idDetailPengembalian, double jumlahDenda) {
        this.idDenda = idDenda;
        this.idDetailPengembalian = idDetailPengembalian;
        this.jumlahDenda = jumlahDenda;
    }

    public int getIdDenda() { return idDenda; }
    public void setIdDenda(int idDenda) { this.idDenda = idDenda; }

    public int getIdDetailPengembalian() { return idDetailPengembalian; }
    public void setIdDetailPengembalian(int idDetailPengembalian) { this.idDetailPengembalian = idDetailPengembalian; }

    public double getJumlahDenda() { return jumlahDenda; }
    public void setJumlahDenda(double jumlahDenda) { this.jumlahDenda = jumlahDenda; }
}