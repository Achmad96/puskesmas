package src.containers;

import src.App;
import src.utils.BookingHelper;
import src.enums.LoggingType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static src.utils.LoggingUtil.logln;

public class BookingContainer implements ActionListener {
    private JPanel bookingPanel;
    private JTextField inputField;
    private JTable table;

    private JButton clearButton;
    private JButton findButton;
    private JButton addButton;
    private JButton backButton;

    private final BookingHelper bookingHelper;

    private final List<String[]> dataList = new ArrayList<>();

    private final String[] columns = {"NOMOR ANTRIAN", "ID PASIEN", "TANGGAL PEMESANAN"};

    public BookingContainer() {
        this.bookingHelper = new BookingHelper();
        intializeEvents();

        this.getAllData();
        this.refreshModel();
    }

    public void intializeEvents() {
        findButton.addActionListener(this);
        addButton.addActionListener(this);
        clearButton.addActionListener(this);
        backButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            App.getInstance().backToHome();
            return;
        }

        if (e.getSource() == addButton) {
            bookingHelper.insertData(inputField.getText().trim());
            inputField.setText("");
            this.getAllData();
            logln("Sucessfully added patient", LoggingType.DEBUG);
        } else if (e.getSource() == findButton) {
            this.getDataById(inputField.getText().trim());
            inputField.setText("");
            if (!dataList.isEmpty()) {
                logln("Booking found", LoggingType.DEBUG);
            } else {
                this.getAllData();
                logln("Booking not found", LoggingType.DEBUG);
            }
        } else if (e.getSource() == clearButton) {
            bookingHelper.deleteAllData();
            bookingHelper.resetAutoIncrement();
            this.getAllData();
            logln("Sucessfully cleared all patients", LoggingType.DEBUG);
        }
        this.refreshModel();
    }

    public void getAllData() {
        try {
            dataList.clear();
            final ResultSet patients = bookingHelper.getAllData();
            while (patients.next()) {
                final String[] row = new String[] {
                    patients.getString("nomor_antrian"),
                    patients.getString("id_pasien"),
                    patients.getString("tanggal_pemesanan")
                };
                dataList.add(row);
            }
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    public void getDataById(String patientId) {
        try {
            dataList.clear();
            final ResultSet patient = bookingHelper.getDataById(patientId);
            if (patient.next()) {
                final String[] row = new String[] {
                        patient.getString("nomor_antrian"),
                        patient.getString("id_pasien"),
                        patient.getString("tanggal_pemesanan")
                };
                dataList.add(row);
            }
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    public void refreshModel() {
        final DefaultTableModel model = new DefaultTableModel();
        for (String column_name : columns) {
            model.addColumn(null, new Object[]{column_name});
        }
        for (String[] data : dataList) {
            model.addRow(data);
        }
        table.setModel(model);
    }

    public JPanel getBookingPanel() {
        return bookingPanel;
    }
}
