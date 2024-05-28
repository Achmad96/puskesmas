package src.utils;

import src.Helper;
import src.enums.LoggingType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static src.utils.LoggingUtil.logln;

public class ExaminationHelper implements Helper {
    @Override public void insertData(String examinationId, HashMap<String,String> dataOptions){
        try {
            final StringBuilder stringBuilder = new StringBuilder(
                    "INSERT INTO pemeriksaan" +
                    " (id_pemeriksaan, ");
            dataOptions.keySet().forEach(k -> stringBuilder.append(k).append(", "));
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(") VALUES (");
            dataOptions.forEach((_, _) -> stringBuilder.append("?, "));
            stringBuilder.append("?)");

            final String sql = stringBuilder.toString();
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, examinationId);
            int i = 2;
            for (String value : dataOptions.values()) {
                preparedStatement.setObject(i++, value);
            }
            preparedStatement.executeUpdate();
            logln("Examination inserted successfully", LoggingType.DEBUG);
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    @Override public void updateData(String examinationId, HashMap<String, String> updateOptions) {
        try {
            final StringBuilder stringBuilder = new StringBuilder("UPDATE pemeriksaan SET ");
            updateOptions.forEach((key, value) -> {
                if (!value.trim().isEmpty()) {
                    stringBuilder.append(key).append(" = ?, ");
                }
            });
            // Remove the last comma and space from the SET part
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(" WHERE id_pemeriksaan = ?");
            final String sql = stringBuilder.toString();
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int i = 1;
            for (String value : updateOptions.values()) {
                if (!value.trim().isEmpty()){
                    preparedStatement.setObject(i++, value);
                }
            }
            preparedStatement.setString(i, examinationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    @Override public ResultSet getAllData() {
        try {
            final String sql =
                    "SELECT * FROM pemeriksaan " +
                    "INNER JOIN public.pasien p on p.id = pemeriksaan.id_pasien";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
            return null;
        }
    }

    @Override public ResultSet getDataById(String id) {
        try {
            final String sql = "SELECT * FROM pemeriksaan WHERE id_pemeriksaan = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.execute();
            return preparedStatement.getResultSet();
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
            return null;
        }
    }

    @Override public void deleteDataById(String examinationId){
        try {
            final String sql = "DELETE FROM pemeriksaan WHERE id_pemeriksaan = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, examinationId);
            preparedStatement.executeUpdate();
        } catch (Exception e){
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    @Override public void deleteAllData() {}

    @Override public void insertData(String id) {}
}
