package src.containers;

import src.App;
import src.enums.LoggingType;
import src.utils.ExaminationHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static src.utils.LoggingUtil.logln;

public class ExaminationContainer implements ActionListener {

    private JPanel examinationPanel;
    private JTable table;

    private JButton backButton;
    private JButton addButton;
    private JButton removeButton;
    private JButton findButton;
    private JButton updateButton;

    private JTextField id_pemeriksaanField;
    private JTextField id_pasienField;
    private JTextField id_nakesField;
    private JTextField keluhanField;
    private JTextField diagnosisField;
    private JTextField tindakanField;
    private JTextField resep_obatField;

    private final ExaminationHelper examinationHelper;
    private final ArrayList<String[]> dataList = new ArrayList<>();

    private final String[] columns = {
            "ID PEMERIKSAAN", "ID NAKES", "ID PASIEN",
            "KELUHAN", "DIAGNOSIS", "TINDAKAN", "RESEP OBAT"};

    public ExaminationContainer() {
        this.examinationHelper = new ExaminationHelper();
        this.initializeEvents();
        this.getAllData();
        this.refreshModel();
    }

    public void initializeEvents() {
        backButton.addActionListener(this);
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        findButton.addActionListener(this);
        removeButton.addActionListener(this);

    }

    public JPanel getExaminationPanel() {
        return examinationPanel;
    }

    public void getAllData() {
        try {
            dataList.clear();
            final ResultSet examinationData = examinationHelper.getAllData();
            while (examinationData.next()) {
                final String[] row = new String[] {
                        examinationData.getString("id_pemeriksaan"),
                        examinationData.getString("id_nakes"),
                        examinationData.getString("id_pasien"),
                        examinationData.getString("keluhan"),
                        examinationData.getString("diagnosis"),
                        examinationData.getString("tindakan"),
                        examinationData.getString("resep_obat"),
                };
                dataList.add(row);
            }
        } catch (SQLException e) {
            logln(e.getMessage(), LoggingType.ERROR);
        }
    }

    public void getDataById(String examinationId) {
        try {
            dataList.clear();
            final ResultSet examinationData = examinationHelper.getDataById(examinationId);
            if (examinationData.next()) {
                final String[] row = new String[] {
                        examinationData.getString("id_pemeriksaan"),
                        examinationData.getString("id_nakes"),
                        examinationData.getString("id_pasien"),
                        examinationData.getString("keluhan"),
                        examinationData.getString("diagnosis"),
                        examinationData.getString("tindakan"),
                        examinationData.getString("resep_obat"),
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
            model.addColumn("Columns", new Object[]{column_name});
        }
        for (String[] data : dataList) {
            model.addRow(data);
        }
        table.setModel(model);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            App.getInstance().backToHome();
            return;
        }

        if (e.getSource() == addButton) {
            final HashMap<String, String> insertOptions = this.getOptions();
            examinationHelper.insertData(id_pemeriksaanField.getText().trim(), insertOptions);
            this.getAllData();
        } else if (e.getSource() == removeButton) {
            final String id_pemeriksaan = !id_pemeriksaanField.getText().trim().isEmpty() ? id_pemeriksaanField.getText().trim() : table.getValueAt(table.getSelectedRow(), 0).toString();
            examinationHelper.deleteDataById(id_pemeriksaan);
            this.getAllData();
        } else if (e.getSource() == updateButton) {
            final String id_pemeriksaan = !id_pemeriksaanField.getText().trim().isEmpty() ? id_pemeriksaanField.getText().trim() : table.getValueAt(table.getSelectedRow(), 0).toString();
            examinationHelper.updateData(id_pemeriksaan, this.getOptions());
            this.getAllData();
        } else if (e.getSource() == findButton) {
            this.getDataById(id_pemeriksaanField.getText().trim());
            id_pemeriksaanField.setText("");
            if (!dataList.isEmpty()) {
                logln("Examination found", LoggingType.DEBUG);
            } else {
                this.getAllData();
                logln("Examination not found", LoggingType.DEBUG);
            }
        }
        this.refreshModel();
    }

    private HashMap<String, String> getOptions() {
        final HashMap<String, String> options = new HashMap<>();
        options.put("id_nakes", id_nakesField.getText().trim());
        options.put("id_pasien", id_pasienField.getText().trim());
        options.put("keluhan", keluhanField.getText().trim());
        options.put("diagnosis", diagnosisField.getText().trim());
        options.put("tindakan", tindakanField.getText().trim());
        options.put("resep_obat", resep_obatField.getText().trim());
        return options;
    }
}
