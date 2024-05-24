package src.helper;

import src.enums.LoggingType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static src.helper.LoggingHelper.logln;

public class BookingHelper {
    private final Connection connection = ConnectionHelper.getConnectionHelper().getConnection();

    public void insertNumber(String idPasien) {
        try {
            final String sql = "INSERT INTO pemesanan_nomor_antrian(id_pasien) VALUES (?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, idPasien);
            ps.execute();
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    public void clearAllPasien() {
        try {
            final String sql = "DELETE FROM pemesanan_nomor_antrian";
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.execute();
        } catch (SQLException exception) {
            logln(exception.getMessage(), LoggingType.ERROR);
        }
    }

    public ResultSet getPasienById(String idPasien){
        try {
            final String sql = "SELECT * FROM pemesanan_nomor_antrian WHERE id_pasien = ?";
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, idPasien);
            ps.executeQuery();
            return ps.getResultSet();
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
            return null;
        }
    }

    public ResultSet getAllPasien() {
        try {
            final String sql = "SELECT * FROM pemesanan_nomor_antrian";
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeQuery();
            return ps.getResultSet();
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
            return null;
        }
    }
}
