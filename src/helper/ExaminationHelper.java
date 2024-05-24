package src.helper;

import src.enums.LoggingType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static src.helper.LoggingHelper.logln;

public class ExaminationHelper {
    private final Connection connection = ConnectionHelper.getConnectionHelper().getConnection();

    public ResultSet getAllExaminations() {
        try {
            final String sql =
                    "SELECT * FROM pemeriksaan " +
                    "INNER JOIN public.pasien p on p.id = pemeriksaan.id_pasien";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertExamination(String examinationId, HashMap<String,String> insertOptions){
        try {
            final StringBuilder stringBuilder = new StringBuilder(
                    "INSERT INTO pemeriksaan" +
                    " (id_pemeriksaan, ");
            insertOptions.keySet().forEach(k -> stringBuilder.append(k).append(", "));
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(") VALUES (");
            insertOptions.forEach((_, _) -> stringBuilder.append("?, "));
            stringBuilder.append("?)");

            final String sql = stringBuilder.toString();
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, examinationId);
            int i = 2;
            for (String value : insertOptions.values()) {
                preparedStatement.setObject(i++, value);
            }

            preparedStatement.executeUpdate();
            logln("Examination inserted successfully", LoggingType.DEBUG);
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    public void updateExamination(String examinationId, HashMap<String, String> updateOptions) {
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
                if(!value.trim().isEmpty()){
                    preparedStatement.setObject(i++, value);
                }
            }
            preparedStatement.setString(i, examinationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    public void removeExamination(String examinationId){
        try {
            final String sql = "DELETE FROM pemeriksaan WHERE id_pemeriksaan = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, examinationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

}
