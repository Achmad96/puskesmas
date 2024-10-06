package src.containers;

import src.App;
import src.helpers.ExaminationHelper;

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

public class ExaminationContainer implements ActionListener, ListSelectionListener {

    private JPanel examinationPanel;
    private JTable table;

    private JButton backButton;
    private JButton addButton;
    private JButton removeButton;
    private JButton findButton;
    private JButton updateButton;

    private JTextField idField;
    private JTextField idPatientField;
    private JTextField idHealthWorkerField;
    private JTextField keluhanField;
    private JTextField diagnosisField;
    private JTextField tindakanField;
    private JTextField resepObatField;
    private JComboBox pasienComboBox;
    private JComboBox nakesComboBox;


    private final ExaminationHelper examinationHelper = new ExaminationHelper();
    private final ArrayList<String[]> dataList = new ArrayList<>();
    private final ArrayList<String[]> patientsList = new ArrayList<>();
    private final ArrayList<String[]> healthWorkersList = new ArrayList<>();

    private final String[] columns = {"ID PEMERIKSAAN", "ID NAKES", "ID PASIEN", "KELUHAN", "DIAGNOSIS", "TINDAKAN", "RESEP OBAT"};


    public ExaminationContainer() {
        this.getAllData();
        initializeComboBox();
        initializeEvents();

        this.idField.setText(generateRandomID("PEM"));
        this.idHealthWorkerField.setText(healthWorkersList.getFirst()[0]);
        this.idPatientField.setText(patientsList.getFirst()[0]);
        this.refreshTableModel();
    }

    private void initializeComboBox() {
        try {
            patientsList.clear();
            healthWorkersList.clear();
            final ResultSet patientsResult = examinationHelper.getAllPatients();
            while (patientsResult.next()) {
                patientsList.add(new String[]{patientsResult.getString(1), patientsResult.getString(2)});
                pasienComboBox.addItem(patientsResult.getString(2));
            }
            final ResultSet healthWorkersResult = examinationHelper.getAllHealthWorkers();
            while (healthWorkersResult.next()) {
                healthWorkersList.add(new String[]{healthWorkersResult.getString(1), healthWorkersResult.getString(2)});
                nakesComboBox.addItem(healthWorkersResult.getString(2));
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
            examinationHelper.insertData(id, this.getOptions());
        } else if (e.getSource() == updateButton) {
            examinationHelper.updateData(id, this.getOptions());
        } else if (e.getSource() == removeButton) {
            examinationHelper.deleteDataById(id);
        }  else if (e.getSource() == findButton) {
            if (this.dataList.size() == 1){
                this.getAllData();
                this.refreshTableModel();
                return;
            }
            final String findId = JOptionPane.showInputDialog("Masukkan id pemeriksaan:");
            if (findId  == null ||  findId.trim().equalsIgnoreCase("")) {
                return;
            }
            this.getDataById(findId);
            this.refreshTableModel();
            return;
        } else if (e.getSource() == nakesComboBox) {
            String idNakes = healthWorkersList.get(nakesComboBox.getSelectedIndex())[0];
            idHealthWorkerField.setText(idNakes);
        } else if (e.getSource() == pasienComboBox) {
            String idPasien = patientsList.get(pasienComboBox.getSelectedIndex())[0];
            idPatientField.setText(idPasien);
        }
        this.getAllData();
        this.refreshTableModel();
    }

    public void initializeEvents() {
        backButton.addActionListener(this);

        table.getSelectionModel().addListSelectionListener(this);

        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        findButton.addActionListener(this);
        removeButton.addActionListener(this);

        nakesComboBox.addActionListener(this);
        pasienComboBox.addActionListener(this);
    }

    public void getAllData() {
        try {
            dataList.clear();
            final ResultSet data = examinationHelper.getAllData();
            while (data.next()) {
                final String[] row = new String[]{
                        data.getString("id"),
                        data.getString("id_nakes"),
                        data.getString("id_pasien"),
                        data.getString("keluhan"),
                        data.getString("diagnosis"),
                        data.getString("tindakan"),
                        data.getString("resep_obat")};
                dataList.add(row);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }


    }

    public void getDataById(String id) {
        try {
            dataList.clear();
            final ResultSet data = examinationHelper.getDataById(id);
            if (data.next()) {
                final String[] row = new String[]{
                        data.getString("id"),
                        data.getString("id_nakes"),
                        data.getString("id_pasien"),
                        data.getString("keluhan"),
                        data.getString("diagnosis"),
                        data.getString("tindakan"),
                        data.getString("resep_obat")};
                dataList.add(row);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void refreshTableModel() {
        final DefaultTableModel model = new DefaultTableModel();
        for (String column_name : columns) {
            model.addColumn("Columns", new Object[]{column_name});
        }
        for (String[] strings : dataList) {
            model.addRow(strings);
        }
        table.setModel(model);
    }

    public JPanel getExaminationPanel() {
        return examinationPanel;
    }

    private HashMap<String, Object> getOptions() {
        final HashMap<String, Object> options = new HashMap<>();
        options.put("id_nakes", idHealthWorkerField.getText().trim());
        options.put("id_pasien", idPatientField.getText().trim());
        options.put("keluhan", keluhanField.getText().trim());
        options.put("diagnosis", diagnosisField.getText().trim());
        options.put("tindakan", tindakanField.getText().trim());
        options.put("resep_obat", resepObatField.getText().trim());
        return options;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (table.getSelectedRow() < 1) {
            return;
        }
        final String selectedId = table.getValueAt(table.getSelectedRow(), 0).toString();
        idField.setText(selectedId);
    }
}