package src.containers;

import src.App;
import src.helpers.BookingHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingContainer implements ActionListener {
    private JPanel bookingPanel;
    private JTextField idPatientField;
    private JTable table;

    private JButton clearButton;
    private JButton findButton;
    private JButton addButton;
    private JButton backButton;
    private JComboBox<String> bookingComboBox;

    private final String[] columns = {"Nomor Antrian", "Id Pasien", "Nama Pasien", "Tanggal Pemesanan"};

    private final BookingHelper bookingHelper = new BookingHelper();
    private final ArrayList<String[]> dataList = new ArrayList<>();
    private final ArrayList<String[]> patientsList = new ArrayList<>();

    public BookingContainer() {
        boldingTheHeaders();

        initializeComboBox();
        intializeEvents();
        this.getAllData();
        this.refreshTableModel();
    }

    private void boldingTheHeaders() {
        final JTableHeader jTableHeader = table.getTableHeader();
        jTableHeader.setFont(new Font("Serif", Font.BOLD, 15));
        table.setTableHeader(jTableHeader);
    }

    public void intializeEvents() {
        findButton.addActionListener(this);
        addButton.addActionListener(this);
        clearButton.addActionListener(this);
        backButton.addActionListener(this);
        bookingComboBox.addActionListener(this);
    }

    public void initializeComboBox() {
        try {
            final ResultSet patientsResult = bookingHelper.getAllPatients();
            while (patientsResult.next()) {
                patientsList.add(new String[]{patientsResult.getString(1), patientsResult.getString(2)});
            }
            final String[] resultList = patientsList.stream().map(array -> array[1]).toList().toArray(new String[patientsList.size()]);
            final DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(resultList);
            bookingComboBox.setModel(comboBoxModel);
            idPatientField.setText(patientsList.getFirst()[0]);
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == backButton) {
            App.getInstance().backToHome();
            return;
        }

        final String currentPatientId = idPatientField.getText().trim();
        if (event.getSource() == addButton) {
            bookingHelper.insertData(currentPatientId);
        } else if (event.getSource() == findButton) {
            if (this.dataList.size() == 1){
                this.getAllData();
                this.refreshTableModel();
                return;
            }
            final String findId = JOptionPane.showInputDialog("Masukkan nomor antrian:");
            if (findId == null || findId.trim().equalsIgnoreCase("")) {
                return;
            }
            this.getDataById(findId);
            this.refreshTableModel();
            idPatientField.setText("");
            return;
        } else if (event.getSource() == clearButton) {
            bookingHelper.deleteAllData();
            bookingHelper.resetAutoIncrement();
        } else if (event.getSource() == bookingComboBox) {
            final String idPatient = patientsList.get(bookingComboBox.getSelectedIndex())[0];
            idPatientField.setText(idPatient);
            return;
        }
        this.getAllData();
        this.refreshTableModel();
    }

    public void getAllData() {
        try {
            dataList.clear();
            final ResultSet data = bookingHelper.getAllData();
            while (data.next()) {
                final String[] row = new String[]{
                        data.getString("id_pemesanan"),
                        data.getString("id_pasien"),
                        data.getString("nama"),
                        data.getString("tanggal_pemesanan")};
                dataList.add(row);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void getDataById(String id) {
        try {
            dataList.clear();
            final ResultSet data = bookingHelper.getDataById(id);
            if (data.next()) {
                final String[] row = new String[]{
                        data.getString("id_pemesanan"),
                        data.getString("id_pasien"),
                        data.getString("nama"),
                        data.getString("tanggal_pemesanan")};
                dataList.add(row);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void refreshTableModel() {
        final Object[][] array2D = new Object[dataList.size()][];
        final DefaultTableModel model = new DefaultTableModel(dataList.toArray(array2D), columns);
        table.setModel(model);
    }

    public JPanel getBookingPanel() {
        return bookingPanel;
    }
}
