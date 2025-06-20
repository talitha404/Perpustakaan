package perpustakaan;

import java.sql.*;
import java.util.*;

public class daoDenda {
    private Connection conn;

    public daoDenda(Connection conn) {
        this.conn = conn;
    }

    public void jalankanProsedurHitungDenda() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CALL hitung_denda()");
        }
    }
} 