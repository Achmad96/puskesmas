package src.utils;

import src.enums.LoggingType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static src.utils.LoggingUtil.logln;

public class BookingHelper {
    private final Connection connection = ConnectionHelper.getConnectionHelper().getConnection();

    public void insertData(String id) {
        try {
            final String sql = "INSERT INTO pemesanan_nomor_antrian(id_pasien) VALUES (?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    public void clearAllData() {
        try {
            final String sql = "DELETE FROM pemesanan_nomor_antrian";
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.execute();
        } catch (SQLException exception) {
            logln(exception.getMessage(), LoggingType.ERROR);
        }
    }

    public ResultSet getAllData() {
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

    public ResultSet getDataById(String id) {
        try {
            final String sql = "SELECT * FROM pemesanan_nomor_antrian WHERE id_pasien = ?";
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeQuery();
            return ps.getResultSet();
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
            return null;
        }
    }
}
