package src.utils;

import src.enums.LoggingType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static src.utils.LoggingUtil.logln;

public class PayingHelper {
    private final Connection connection = ConnectionHelper.getConnectionHelper().getConnection();

    public void insertData(String payingId, HashMap<String, String> dataOptions) {
        try {
            final StringBuilder stringBuilder = new StringBuilder(
                    "INSERT INTO pembayaran_obat" +
                    " (id_pembayaran, ");
            dataOptions.keySet().forEach(k -> stringBuilder.append(k).append(", "));
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(") VALUES (");
            dataOptions.forEach((_, _) -> stringBuilder.append("?, "));
            stringBuilder.append("?)");

            final String sql = stringBuilder.toString();
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, payingId);

            int i = 2;
            for (String value : dataOptions.values()) {
                preparedStatement.setObject(i++, value);
            }

            preparedStatement.executeUpdate();
            logln("Paying inserted successfully", LoggingType.DEBUG);
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    public ResultSet getAllData() {
        try {
            final String sql =
                    "SELECT * FROM pembayaran_obat " +
                    "INNER JOIN public.pasien p on p.id = pembayaran_obat.id_pasien";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getDataById(String id) {
        try {
            final String sql = "SELECT * FROM pembayaran_obat WHERE id_pembayaran = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
            return null;
        }
    }
}