package src.helpers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PaymentHelper extends Helper {

    public PaymentHelper() {
        super("pembayaran_obat");
    }

    @Override
    public ResultSet getAllData() {
        try {
            this.createConnection();
            final String sql =
                    "SELECT po.id AS id_pembayaran, id_kasir, id_pasien, po.id_obat AS id_obat, dp.jumlah_obat AS jumlah_obat" +
                            " FROM pembayaran_obat po" +
                            " INNER JOIN detail_pembayaran dp ON dp.id = po.id";
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            System.out.println(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
    }

    public ResultSet getMedicines() {
        try {
            this.createConnection();
            final String sql = "SELECT * FROM obat";
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            System.out.println(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
    }

    public ResultSet getPatients() {
        try {
            this.createConnection();
            final String sql = "SELECT * FROM pasien";
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            System.out.println(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
    }

    public ResultSet getCashiers() {
        try {
            this.createConnection();
            final String sql = "SELECT * FROM kasir";
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            System.out.println(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
    }

    public int insertData(String id, HashMap<String, Object> options, String idObat, int jumlahObat) {
        try {
            this.createConnection();
            this.getConnection().setAutoCommit(false);
            super.setTableName("pembayaran_obat");
            super.insertData(id, options);

            super.setTableName("detail_pembayaran");
            final HashMap<String, Object> detailOptions = new HashMap<>();
            detailOptions.put("id_obat", options.get("id_obat"));
            detailOptions.put("jumlah_obat", jumlahObat);
            super.insertData(id, detailOptions);

            final String updateStockQuery = "UPDATE obat SET stok = stok - ? WHERE id = ?";
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(updateStockQuery);

            preparedStatement.setInt(1, jumlahObat);
            preparedStatement.setString(2, idObat);
            preparedStatement.executeUpdate();

            this.getConnection().commit();
            System.out.println("Stock updated successfully!");

            final String querySelect = "SELECT stok FROM obat WHERE id = ?";
            preparedStatement = this.getConnection().prepareStatement(querySelect);
            preparedStatement.setString(1, idObat);
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();
            return resultSet.getInt("stok");
        } catch (SQLException exception) {
            if (this.getConnection() != null) {
                try {
                    this.getConnection().rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println(rollbackEx.getMessage());
                }
            }
            System.err.println("Error: " + exception.getMessage());
            return -1;
        } finally {
            this.closeConnection();
        }
    }
}