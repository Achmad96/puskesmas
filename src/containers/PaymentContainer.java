package src.containers;

import src.App;
import src.enums.LoggingType;
import src.utils.PaymentHelper;

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

public class PaymentContainer implements ActionListener {

    private JPanel PaymentPanel;
    private JTable table;

    private JButton findButton;
    private JButton addButton;
    private JButton backButton;

    private JTextField id_pembayaranField;
    private JTextField id_kasirField;
    private JTextField id_pasienField;
    private JTextField id_obatField;
    private JTextField jumlah_obatField;

    final PaymentHelper paymentHelper = new PaymentHelper();
    private final List<String[]> dataList = new ArrayList<>();

    private final String[] columns = {
            "ID PEMBAYARAN", "ID KASIR", "ID PASIEN",
            "ID OBAT", "JUMLAH OBAT"};

    public JPanel getPaymentPanel() {
        return PaymentPanel;
    }

    public PaymentContainer() {
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
            final ResultSet paymentData = paymentHelper.getAllData();
            while (paymentData.next()) {
                final String[] row = new String[] {
                        paymentData.getString("id_pembayaran"),
                        paymentData.getString("id_kasir"),
                        paymentData.getString("id_pasien"),
                        paymentData.getString("id_obat"),
                        paymentData.getString("jumlah_obat"),
                };
                dataList.add(row);
            }
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    public void getPatientById(String paymentId) {
        try {
            dataList.clear();
            final ResultSet paymentData = paymentHelper.getDataById(paymentId);
            if (paymentData.next()) {
                final String[] row = new String[] {
                        paymentData.getString("id_pembayaran"),
                        paymentData.getString("id_kasir"),
                        paymentData.getString("id_pasien"),
                        paymentData.getString("id_obat"),
                        paymentData.getString("jumlah_obat"),
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
            return;
        }

        if (e.getSource() == addButton) {
            final String id_pembayaran = !id_pembayaranField.getText().trim().isEmpty() ? id_pembayaranField.getText().trim() : table.getValueAt(table.getSelectedRow(), 0).toString();
            paymentHelper.insertData(id_pembayaran, this.getOptions());
            this.getAllData();
        } else if (e.getSource() == findButton) {
            this.getPatientById(id_pembayaranField.getText().trim());
            id_pembayaranField.setText("");
            if (!dataList.isEmpty()) {
                logln("Payment found", LoggingType.DEBUG);
            } else {
                this.getAllData();
                logln("Payment not found", LoggingType.DEBUG);
            }
        }
        this.refreshModel();

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
