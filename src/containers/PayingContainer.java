package src.containers;

import src.App;
import src.enums.LoggingType;
import src.utils.PayingHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static src.utils.LoggingUtil.logln;

public class PayingContainer implements ActionListener {

    private JPanel PayingPanel;
    private JTable table;

    private JButton findButton;
    private JButton addButton;
    private JButton backButton;

    private JTextField id_pembayaranField;
    private JTextField id_kasirField;
    private JTextField id_pasienField;
    private JTextField id_obatField;
    private JTextField jumlah_obatField;

    final PayingHelper payingHelper = new PayingHelper();
    private final List<String[]> dataList = new ArrayList<>();

    private final String[] columns = {
            "ID PEMBAYARAN", "ID KASIR", "ID PASIEN",
            "ID OBAT", "JUMLAH OBAT"};

    public JPanel getPayingPanel() {
        return PayingPanel;
    }

    public PayingContainer() {
        this.initializeEvents();
        this.getAllData();
        this.refreshModel();
    }

    public void initializeEvents() {
        assert  backButton != null &&
                addButton != null &&
                findButton != null;
        backButton.addActionListener(this);
        addButton.addActionListener(this);
        findButton.addActionListener(this);
    }

    public void getAllData() {
        try {
            dataList.clear();
            final ResultSet payingData = payingHelper.getAllData();
            while (payingData.next()) {
                final String[] row = new String[] {
                        payingData.getString("id_pembayaran"),
                        payingData.getString("id_kasir"),
                        payingData.getString("id_pasien"),
                        payingData.getString("id_obat"),
                        payingData.getString("jumlah_obat"),
                };
                dataList.add(row);
            }
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    public void getPatientById(String payingId) {
        try {
            dataList.clear();
            final ResultSet payingData = payingHelper.getDataById(payingId);
            if (payingData.next()) {
                final String[] row = new String[] {
                        payingData.getString("id_pembayaran"),
                        payingData.getString("id_kasir"),
                        payingData.getString("id_pasien"),
                        payingData.getString("id_obat"),
                        payingData.getString("jumlah_obat"),
                };
                dataList.add(row);
            }
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            App.getInstance().backToHome();
        } else if (e.getSource() == addButton) {
            if (id_pembayaranField.getText().trim().isEmpty()) {
                String id_pembayaran = table.getValueAt(table.getSelectedRow(), 0).toString();
                payingHelper.insertData(id_pembayaran, this.getOptions());
            } else {
                payingHelper.insertData(this.id_pembayaranField.getText(), this.getOptions());
            }
            this.getAllData();
            this.refreshModel();
        } else if (e.getSource() == findButton) {
            this.getPatientById(id_pembayaranField.getText().trim());
            id_pembayaranField.setText("");
            if (!dataList.isEmpty()) {
                logln("Patient found", LoggingType.DEBUG);
            } else {
                this.getAllData();
                logln("Patient not found", LoggingType.DEBUG);
            }
            this.refreshModel();
        }

    }

    public void refreshModel() {
        final DefaultTableModel model = new DefaultTableModel();
        for (String column_name : columns) {
            model.addColumn("Columns", new Object[]{column_name});
        }
        for (String[] data : dataList) {
            model.addRow(data);
        }
        table.setModel(model);
    }

    public HashMap<String, String> getOptions(){
        HashMap<String, String> options = new HashMap<>();
        options.put("id_kasir", id_kasirField.getText());
        options.put("id_pasien", id_pasienField.getText());
        options.put("id_obat", id_obatField.getText());
        options.put("jumlah_obat", jumlah_obatField.getText());
        return options;
    }
}
