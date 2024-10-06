package src.helpers;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingHelper extends Helper {

    public BookingHelper() {
        super("pemesanan_nomor_antrian");
    }

    @Override public void insertData(String id){
        try {
            final String sql = "INSERT INTO " + super.getTableName() + " (id_pasien) VALUES (?)";
            final PreparedStatement ps = this.getConnection().prepareStatement(sql);
            System.out.println(sql);
            ps.setString(1, id);
            ps.execute();
            System.out.println("Sucessfully insert a data!");
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            JOptionPane.showMessageDialog(null, "Maaf, terjadi kesalahan!\n" + exception.getMessage());
        }
    }

    @Override public ResultSet getAllData() {
        try {
            final String sql = "SELECT pna.id AS id_pemesanan, id_pasien, p.nama AS nama, tanggal_pemesanan FROM pemesanan_nomor_antrian pna " +
                               "INNER JOIN public.pasien p on p.id = pna.id_pasien";
            final PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.executeQuery();
            return ps.getResultSet();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public ResultSet getAllPatients() {
        try {
            final String sql = "SELECT id, nama FROM pasien";
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override public ResultSet getDataById(String id) {
        try {
            final String sql =
                    "SELECT pna.id AS id_pemesanan, id_pasien, p.nama, tanggal_pemesanan FROM " +
                    super.getTableName()
                    + " pna INNER JOIN public.pasien p on p.id = pna.id_pasien " + " WHERE pna.id = ?";
            final PreparedStatement ps = this.getConnection().prepareStatement(sql);
            System.out.println(sql);
            ps.setInt(1, Integer.parseInt(id));
            ps.executeQuery();
            return ps.getResultSet();
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            JOptionPane.showMessageDialog(null, "Maaf, terjadi kesalahan!\n" + exception.getMessage());
            return null;
        }
    }

    public void resetAutoIncrement() {
        try {
            final String sql = "ALTER SEQUENCE pemesanan_nomor_antrian_nomor_antrian_seq RESTART WITH 1";
            final PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

}
