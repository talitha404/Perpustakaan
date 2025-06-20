package perpustakaan;
public class cUser {
    private int idUser;
    private String nama;
    private String role;
    private String password;
    private String email;
    private String telepon;
    private String alamat;

    // Konstruktor
    public cUser() {}

    public cUser(int idUser, String nama, String role, String password, String email, String telepon, String alamat) {
        this.idUser = idUser;
        this.nama = nama;
        this.role = role;
        this.password = password;
        this.email = email;
        this.telepon = telepon;
        this.alamat = alamat;
    }

    // Getter dan Setter
    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
}