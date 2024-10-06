package src.helpers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExaminationHelper extends Helper {
    public ExaminationHelper() {
        super("pemeriksaan");
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

    public ResultSet getAllHealthWorkers() {
        try {
            final String sql = "SELECT id, nama FROM nakes";
            final PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
