package src.containers;

import src.App;
import src.helpers.HealthWorkerHelper;
import src.utils.ButtonGroupUtil;

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

public class HealthWorkerContainer implements ActionListener, ListSelectionListener {
    private JPanel healthWorkerPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField umurField;
    private JTextField jenisKelaminField;
    private JTextField noTeleponField;

    private JComboBox poliComboBox;
    private JComboBox jenisNakesComboBox;

    private JTable table;
    private JButton addButton;
    private JButton backButton;
    private JButton updateButton;
    private JButton removeButton;
    private JLabel title;
    private JRadioButton lakiLakiRadioButton;
    private JRadioButton perempuanRadioButton;

    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final ArrayList<String[]> dataList = new ArrayList<>();
    private final HealthWorkerHelper healthWorkerHelper = new HealthWorkerHelper();
    private final String[] columns = {"id", "nama", "umur", "jenis_kelamin", "no_telp", "poli", "jenis_nakes"};

    public JPanel getHealthWorkerPanel() {
        return healthWorkerPanel;
    }

    public HealthWorkerContainer() {
        this.initializePoliComboBox();
        this.initializeJenisNakesComboBox();
        initializeRadioButtons();
        initializeEvents();

        this.idField.setText(generateRandomID("NAK"));
        this.getAllData();
        this.refreshTableModel();
    }

    public void initializeRadioButtons() {
        buttonGroup.add(lakiLakiRadioButton);
        buttonGroup.add(perempuanRadioButton);
        lakiLakiRadioButton.setSelected(true);
    }

    private void initializePoliComboBox() {
        String[] poliList = {"Umum", "Jantung", "Anak", "Lansia"};
        DefaultComboBoxModel<String> poliComboBoxModel = new DefaultComboBoxModel<>(poliList);
        poliComboBox.setModel(poliComboBoxModel);
    }

    private void initializeJenisNakesComboBox() {
        String[] jenisList = {"Dokter", "Suster"};
        DefaultComboBoxModel<String> jenisNakesComboBoxModel = new DefaultComboBoxModel<>(jenisList);
        jenisNakesComboBox.setModel(jenisNakesComboBoxModel);
    }

    private void initializeEvents() {
        assert backButton != null &&
                addButton != null &&
                updateButton != null &&
                removeButton != null;
        table.getSelectionModel().addListSelectionListener(this);
        backButton.addActionListener(this);
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        removeButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            App.getInstance().backToHome();
            return;
        }

        final String id = idField.getText().trim();
        if (e.getSource() == addButton) {
            healthWorkerHelper.insertData(id, this.getOptions());
        } else if (e.getSource() == updateButton) {
            healthWorkerHelper.updateData(id, this.getOptions());
        } else if (e.getSource() == removeButton) {
            healthWorkerHelper.deleteDataById(id);
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

    private void getAllData() {
        try {
            dataList.clear();
            final ResultSet data = healthWorkerHelper.getAllData();
            while (data.next()) {
                final String[] row = new String[]{
                        data.getString("id"),
                        data.getString("nama"),
                        data.getString("umur"),
                        data.getString("jenis_kelamin"),
                        data.getString("no_telp"),
                        data.getString("poli"),
                        data.getString("jenis_nakes"),
                };
                dataList.add(row);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private HashMap<String, Object> getOptions() {
        HashMap<String, Object> options = new HashMap<>();
        options.put("nama", namaField.getText().trim());
        options.put("umur", Integer.parseInt(umurField.getText().trim()));
        options.put("jenis_kelamin", Objects.requireNonNull(ButtonGroupUtil.getSelectedButtonText(buttonGroup)).toUpperCase().charAt(0));
        options.put("no_telp", noTeleponField.getText());
        options.put("poli", Objects.requireNonNull(poliComboBox.getSelectedItem()).toString());
        options.put("jenis_nakes", Objects.requireNonNull(jenisNakesComboBox.getSelectedItem()).toString());
        return options;
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
        noTeleponField.setText(dataList.get(row - 1)[4]);
    }
}
