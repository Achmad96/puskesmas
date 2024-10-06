package src.helpers;

import src.utils.ConnectionUtil;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;

public class Helper {
    private final Connection connection = ConnectionUtil.getConnectionUtil().getConnection();
    private String tableName;

    public Helper(String tableName) {
        this.tableName = tableName;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ResultSet getDataById(String id) {
        try {
            final String sql =
                    "SELECT * FROM " + tableName +
                    " WHERE id = ?";
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeQuery();
            return ps.getResultSet();
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            JOptionPane.showMessageDialog(null, "Maaf, terjadi kesalahan!\n" + exception.getMessage());
            return null;
        }
    }

    public ResultSet getAllData() {
        try {
            final String sql = "SELECT * FROM " + tableName;
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            System.out.println(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            JOptionPane.showMessageDialog(null, "Maaf, terjadi kesalahan!\n" + exception.getMessage());
            return null;
        }
    }

    public void deleteDataById(String id) {
        try {
            final String sql = "DELETE FROM " + tableName + " WHERE id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            System.out.println(sql);
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            JOptionPane.showMessageDialog(null, "Maaf, terjadi kesalahan!\n" + exception.getMessage());
        }
    }

    public void deleteAllData() {
        try {
            final String sql = "DELETE FROM " + tableName;
            final PreparedStatement ps = this.getConnection().prepareStatement(sql);
            System.out.println(sql);
            ps.execute();
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            JOptionPane.showMessageDialog(null, "Maaf, terjadi kesalahan!\n" + exception.getMessage());
        }
    }

    public void insertData(String id) {
        try {
            final String sql = "INSERT INTO " + tableName + "(id) VALUES (?)";
            final PreparedStatement ps = this.getConnection().prepareStatement(sql);
            System.out.println(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            JOptionPane.showMessageDialog(null, "Maaf, terjadi kesalahan!\n" + exception.getMessage());
        }
    }

    public void insertData(String id, HashMap<String, Object> options) {
        try {
            final StringBuilder stringBuilder = new StringBuilder(
                    "INSERT INTO " + tableName +
                            " (id " + ", ");
            options.keySet().forEach(k -> stringBuilder.append(k).append(", "));
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(") VALUES (");
            options.forEach((_, _) -> stringBuilder.append("?, "));
            stringBuilder.append("?)");

            final String sql = stringBuilder.toString();
            System.out.println(sql);
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, id);
            int i = 2;
            for (Object value : options.values()) {
                preparedStatement.setObject(i++, value);
            }
            preparedStatement.executeUpdate();
            System.out.println(tableName + " inserted successfully");
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            JOptionPane.showMessageDialog(null, "Maaf, terjadi kesalahan!\n" + exception.getMessage());
        }
    }

    public void updateData(String id, HashMap<String, Object> updateOptions) {
        try {
            final StringBuilder stringBuilder = new StringBuilder("UPDATE " + tableName + " SET ");
            updateOptions.forEach((key, value) -> {
                if (!value.toString().trim().isEmpty()) {
                    stringBuilder.append(key).append(" = ?, ");
                }
            });
            // Remove the last comma and space from the SET part
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(" WHERE id").append(" = ?");
            final String sql = stringBuilder.toString();
            System.out.println(sql);
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            int i = 1;
            for (Object value : updateOptions.values()) {
                if (!value.toString().trim().isEmpty()) {
                    preparedStatement.setObject(i++, value);
                }
            }
            preparedStatement.setString(i, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            JOptionPane.showMessageDialog(null, "Maaf, terjadi kesalahan!\n" + exception.getMessage());
        }
    }
}
