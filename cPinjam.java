package perpustakaan;
import java.sql.Date;

public class cPinjam {
    private int idPinjam;
    private int idUser;
    private Date tanggalPinjam;
    private Date tanggalJatuhTempo;

    public cPinjam() {}

    public cPinjam(int idPinjam, int idUser, Date tanggalPinjam, Date tanggalJatuhTempo) {
        this.idPinjam = idPinjam;
        this.idUser = idUser;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
    }

    public int getIdPinjam() { return idPinjam; }
    public void setIdPinjam(int idPinjam) { this.idPinjam = idPinjam; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public Date getTanggalPinjam() { return tanggalPinjam; }
    public void setTanggalPinjam(Date tanggalPinjam) { this.tanggalPinjam = tanggalPinjam; }

    public Date getTanggalJatuhTempo() { return tanggalJatuhTempo; }
    public void setTanggalJatuhTempo(Date tanggalJatuhTempo) { this.tanggalJatuhTempo = tanggalJatuhTempo; }
}