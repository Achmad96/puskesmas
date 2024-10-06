package src.containers;

import src.App;
import src.utils.ButtonGroupUtil;
import src.helpers.PatientHelper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static src.utils.RandomGeneratedId.generateRandomID;

public class PatientContainer implements ActionListener, ListSelectionListener {
    private JPanel patientPanel;
    private JTextField idField;
    private JTable table;
    private JTextField namaField;
    private JTextField umurField;
    private JTextField nomorTeleponField;
    private JTextField alamatField;
    private JButton addButton;
    private JButton removeButton;
    private JButton backButton;
    private JButton updateButton;
    private JRadioButton lakiLakiRadioButton;
    private JRadioButton perempuanRadioButton;
    private JLabel title;

    private final PatientHelper patientHelper = new PatientHelper();
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final ArrayList<String[]> dataList = new ArrayList<>();

    private final String[] columns = {"ID PASIEN", "NAMA", "UMUR", "NOMER TELEPON", "ALAMAT", "JENIS KELAMIN"};

    public PatientContainer() {
        idField.setText(generateRandomID("PAS"));
        initializeRadioButtons();
        initializeEvents();
        this.getAllData();
        this.refreshTableModel();
    }

    public void initializeEvents() {
        backButton.addActionListener(this);

        table.getSelectionModel().addListSelectionListener(this);
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        removeButton.addActionListener(this);
    }

    public void initializeRadioButtons() {
        buttonGroup.add(lakiLakiRadioButton);
        buttonGroup.add(perempuanRadioButton);
        lakiLakiRadioButton.setSelected(true);
    }

    public void getAllData() {
        try {
            dataList.clear();
            final ResultSet data = patientHelper.getAllData();
            while (data.next()) {
                final String[] row = new String[]{
                        data.getString("id"),
                        data.getString("nama"),
                        data.getString("umur"),
                        data.getString("no_telp"),
                        data.getString("alamat"),
                        data.getString("jenis_kelamin"),
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

        final String id = idField.getText().trim();
        if (e.getSource() == addButton) {
            patientHelper.insertData(id, this.getOptions());
        } else if (e.getSource() == updateButton) {
            patientHelper.updateData(id, this.getOptions());
        } else if (e.getSource() == removeButton) {
            patientHelper.deleteDataById(id);
        }
        this.getAllData();
        this.refreshTableModel();
    }

    public void refreshTableModel() {
        final DefaultTableModel model = new DefaultTableModel();
        for (String column_name : columns) {
            model.addColumn("Columns", new Object[]{column_name});
        }
        for (String[] data : dataList) {
            model.addRow(data);
        }
        table.setModel(model);
    }

    private HashMap<String, Object> getOptions() {
        final HashMap<String, Object> options = new HashMap<>();
        options.put("nama", namaField.getText().trim());
        options.put("umur", Integer.parseInt(umurField.getText().trim()));
        options.put("alamat", alamatField.getText().trim());
        options.put("no_telp", nomorTeleponField.getText().trim());
        options.put("jenis_kelamin", Objects.requireNonNull(ButtonGroupUtil.getSelectedButtonText(buttonGroup)).toUpperCase().charAt(0));
        return options;
    }

    public JPanel getPatientPanel() {
        return patientPanel;
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (table.getSelectedRow() < 1) {
            return;
        }
        final int row = table.getSelectedRow();
        final String selectedId = table.getValueAt(row, 0).toString();
        idField.setText(selectedId);
        namaField.setText(dataList.get(row - 1)[1]);
        umurField.setText(dataList.get(row - 1)[2]);
        nomorTeleponField.setText(dataList.get(row - 1)[3]);
        alamatField.setText(dataList.get(row - 1)[4]);
    }
}
