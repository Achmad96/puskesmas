package src.containers;

import src.App;
import src.helpers.PaymentHelper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import static src.utils.RandomGeneratedId.generateRandomID;

public class PaymentContainer implements ActionListener, KeyListener, ListSelectionListener {

    private JPanel PaymentPanel;
    private JTable table;

    private JButton findButton;
    private JButton addButton;
    private JButton backButton;

    private JTextField idPembayaranField;
    private JTextField idKasirField;
    private JTextField idPasienField;
    private JTextField idObatField;
    private JTextField jumlahObatField;

    private JComboBox medicineComboBox;
    private JComboBox patientComboBox;
    private JComboBox cashierComboBox;
    private JLabel totalLabel;
    private JLabel stockLabel;
    private JButton removeButton;

    final PaymentHelper paymentHelper = new PaymentHelper();
    private final ArrayList<String[]> dataList = new ArrayList<>();
    private final ArrayList<String[]> patientsList = new ArrayList<>();
    private final ArrayList<String[]> cashiersList = new ArrayList<>();
    private final ArrayList<String[]> medicinesList = new ArrayList<>();

    private int stock = 0;
    private int jumlahObat = 1;
    private Double hargaObat = 0.0;
    private Double totalHarga = 0.0;

    private final String[] columns = {"ID PEMBAYARAN", "ID KASIR", "ID PASIEN", "ID OBAT", "JUMLAH OBAT"};

    public PaymentContainer() {
        initializeCashiers();
        initializePatients();
        initializeMedicines();

        this.idKasirField.setText(cashiersList.getFirst()[0]);
        this.idPasienField.setText(patientsList.getFirst()[0]);

        stock = Integer.parseInt(medicinesList.getFirst()[2]);
        hargaObat = Double.parseDouble(medicinesList.getFirst()[3]);
        totalHarga = jumlahObat * hargaObat;
        this.idObatField.setText(medicinesList.getFirst()[0]);
        this.stockLabel.setText(String.valueOf(stock));
        this.totalLabel.setText(String.valueOf(totalHarga));

        this.initializeEvents();

        this.idPembayaranField.setText(generateRandomID("PBO"));
        this.getAllData();
        this.refreshTableModel();
    }

    public JPanel getPaymentPanel() {
        this.initializeMedicines();
        this.getAllData();
        this.refreshTableModel();
        return PaymentPanel;
    }

    public void initializeEvents() {
        assert backButton != null && addButton != null && findButton != null;
        backButton.addActionListener(this);

        table.getSelectionModel().addListSelectionListener(this);
        addButton.addActionListener(this);
        findButton.addActionListener(this);
        removeButton.addActionListener(this);
        cashierComboBox.addActionListener(this);
        patientComboBox.addActionListener(this);
        medicineComboBox.addActionListener(this);

        jumlahObatField.addKeyListener(this);
    }

    public void initializeCashiers() {
        try {
            cashiersList.clear();
            final ResultSet cashierData = paymentHelper.getCashiers();
            while (cashierData.next()) {
                final String[] row = new String[]{cashierData.getString("id"), cashierData.getString("nama"),};
                cashiersList.add(row);
                cashierComboBox.addItem(row[1]);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void initializePatients() {
        try {
            patientsList.clear();
            final ResultSet patientData = paymentHelper.getPatients();
            while (patientData.next()) {
                final String[] row = new String[]{patientData.getString("id"), patientData.getString("nama")};
                patientsList.add(row);
                patientComboBox.addItem(row[1]);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void initializeMedicines() {
        try {
            medicinesList.clear();
            final ResultSet medicineData = paymentHelper.getMedicines();
            while (medicineData.next()) {
                final String[] row = new String[]{
                        medicineData.getString("id"),
                        medicineData.getString("nama"),
                        medicineData.getString("stok"),
                        medicineData.getString("harga")};
                medicinesList.add(row);
                medicineComboBox.addItem(row[1]);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void getAllData() {
        try {
            dataList.clear();
            final ResultSet paymentData = paymentHelper.getAllData();
            while (paymentData.next()) {
                final String[] row = new String[]{
                        paymentData.getString("id_pembayaran"),
                        paymentData.getString("id_kasir"),
                        paymentData.getString("id_pasien"),
                        paymentData.getString("id_obat"),
                        paymentData.getString("jumlah_obat")
                };
                dataList.add(row);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void getDataById(String paymentId) {
        try {
            dataList.clear();
            final ResultSet paymentData = paymentHelper.getDataById(paymentId);
            if (paymentData.next()) {
                final String[] row = new String[]{
                        paymentData.getString("id_pembayaran"),
                        paymentData.getString("id_kasir"),
                        paymentData.getString("id_pasien"),
                        paymentData.getString("id_obat"),
                        paymentData.getString("jumlah_obat")
                };
                dataList.add(row);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            App.getInstance().backToHome();
            return;
        }
        final String idPembayaran = idPembayaranField.getText().trim();
        if (e.getSource() == addButton) {
            final String idObat = idObatField.getText().trim();
            paymentHelper.insertData(idPembayaran, this.getOptions(), jumlahObat);
            paymentHelper.updateStock(idObat, jumlahObat);
        } else if (e.getSource() == findButton) {
            if (this.dataList.size() == 1) {
                this.getAllData();
                this.refreshTableModel();
                return;
            }
            final String findId = JOptionPane.showInputDialog("Masukkan id pembayaran:");
            if (findId == null || findId.trim().equalsIgnoreCase("")) {
                return;
            }
            this.getDataById(findId);
            this.refreshTableModel();
            return;
        } else if (e.getSource() == removeButton) {
            paymentHelper.deleteDataById(idPembayaran);
        } else if (e.getSource() == cashierComboBox) {
            final String idKasir = cashiersList.get(cashierComboBox.getSelectedIndex())[0];
            idKasirField.setText(idKasir);
        } else if (e.getSource() == patientComboBox) {
            final String idPasien = patientsList.get(patientComboBox.getSelectedIndex())[0];
            idPasienField.setText(idPasien);
        } else if (e.getSource() == medicineComboBox) {
            final String idObat = medicinesList.get(medicineComboBox.getSelectedIndex())[0];
            idObatField.setText(idObat);
            stock = Integer.parseInt(medicinesList.get(medicineComboBox.getSelectedIndex())[2]);
            hargaObat = Double.parseDouble(medicinesList.get(medicineComboBox.getSelectedIndex())[3]);
            totalHarga = hargaObat * jumlahObat;
            stockLabel.setText(String.valueOf(stock));
            totalLabel.setText("Rp. " + totalHarga);
        }
        this.getAllData();
        this.refreshTableModel();
    }

    public void refreshTableModel() {
        final DefaultTableModel model = new DefaultTableModel();
        for (String column_name : columns) {
            model.addColumn("Columns", new Object[]{column_name});
        }
        for (String[] data : dataList) model.addRow(data);
        table.setModel(model);
    }

    public HashMap<String, Object> getOptions() {
        HashMap<String, Object> options = new HashMap<>();
        options.put("id_kasir", idKasirField.getText());
        options.put("id_pasien", idPasienField.getText());
        options.put("id_obat", idObatField.getText());
        return options;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() != jumlahObatField) {
            return;
        }
        try {
            int currentJumlahObat = Integer.parseInt(jumlahObatField.getText().trim());
            if (stock - currentJumlahObat < 0) {
                jumlahObatField.setText(String.valueOf(jumlahObat));
                return;
            }
            if (jumlahObatField.getText().trim().isEmpty() || (currentJumlahObat <= 0)) {
                return;
            }
            jumlahObat = Integer.parseInt(jumlahObatField.getText());
            totalHarga = hargaObat * jumlahObat;
            stockLabel.setText(String.valueOf(stock - jumlahObat));
            totalLabel.setText("Rp. " + totalHarga);
        } catch (NumberFormatException ex) {
            jumlahObatField.setText(String.valueOf(jumlahObat));
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (table.getSelectedRow() < 1) {
            return;
        }
        final int row = table.getSelectedRow();
        final String selectedId = table.getValueAt(row, 0).toString();
        idPembayaranField.setText(selectedId);
        idKasirField.setText(dataList.get(row - 1)[1]);
        idPasienField.setText(dataList.get(row - 1)[2]);
        idObatField.setText(dataList.get(row - 1)[3]);
    }
}
