package src.helpers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PaymentHelper extends Helper {

    public PaymentHelper() {
        super("pembayaran_obat");
    }

    public void insertData(String id, HashMap<String, Object> options, int jumlahObat) {
        super.setTableName("pembayaran_obat");
        super.insertData(id, options);

        super.setTableName("detail_pembayaran");
        final HashMap<String, Object> detailOptions = new HashMap<>();
        detailOptions.put("id_obat", options.get("id_obat"));
        detailOptions.put("jumlah_obat", jumlahObat);
        super.insertData(id, detailOptions);
    }

    @Override public ResultSet getAllData() {
        try {
            this.getConnection().setAutoCommit(false);
            final String sql =
                    "SELECT po.id AS id_pembayaran, id_kasir, id_pasien, po.id_obat AS id_obat, dp.jumlah_obat AS jumlah_obat" +
                    " FROM pembayaran_obat po" +
                    " INNER JOIN detail_pembayaran dp ON dp.id = po.id";
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public ResultSet getMedicines() {
        try {
            final String sql = "SELECT * FROM obat";
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public ResultSet getPatients() {
        try {
            final String sql = "SELECT * FROM pasien";
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public ResultSet getCashiers() {
        try {
            final String sql = "SELECT * FROM kasir";
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public void updateStock(String idObat, int jumlahObat) {
        try {
            final String updateStockQuery = "UPDATE obat SET stok = stok + ? WHERE id = ?";
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(updateStockQuery);

            preparedStatement.setInt(1, -jumlahObat);
            preparedStatement.setString(2, idObat);
            preparedStatement.executeUpdate();

            this.getConnection().commit();
            this.getConnection().setAutoCommit(true);
            System.out.println("Stock updated successfully!");

        } catch (SQLException exception) {
            if (this.getConnection() != null) {
                try {
                    this.getConnection().rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println(rollbackEx.getMessage());
                }
            }
            System.err.println(exception.getMessage());
        }
    }
}