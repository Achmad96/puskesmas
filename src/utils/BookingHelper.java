package src.utils;

import src.Helper;
import src.enums.LoggingType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static src.utils.LoggingUtil.logln;

public class BookingHelper implements Helper {
    @Override public void insertData(String id) {
        try {
            final String sql = "INSERT INTO pemesanan_nomor_antrian(id_pasien) VALUES (?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    @Override public ResultSet getAllData() {
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

    @Override public void deleteAllData() {
        try {
            final String sql = "DELETE FROM pemesanan_nomor_antrian";
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.execute();
        } catch (SQLException exception) {
            logln(exception.getMessage(), LoggingType.ERROR);
        }
    }

    @Override public ResultSet getDataById(String id) {
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

    @Override public void insertData(String id, HashMap<String, String> options) {}

    @Override public void updateData(String id, HashMap<String, String> options) {}

    @Override public void deleteDataById(String id) {}

}
