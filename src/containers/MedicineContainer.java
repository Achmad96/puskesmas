package src.containers;

import src.App;
import src.helpers.MedicineHelper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static src.utils.RandomGeneratedId.generateRandomID;

public class MedicineContainer implements ActionListener, ListSelectionListener {
    private JPanel medicinePanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField stockField;
    private JFormattedTextField hargaField;

    private JButton backButton;
    private JTable table;
    private JButton addButton;
    private JButton removeButton;
    private JButton updateButton;

    private final MedicineHelper medicineHelper = new MedicineHelper();
    private final ArrayList<String[]> dataList = new ArrayList<>();
    private final String[] columns = {"Id", "Nama", "Stok", "Harga"};

    public MedicineContainer() {
        boldingTheHeaders();
        initializeEvents();

        this.idField.setText(generateRandomID(10,"OBT"));
        this.getAllData();
        this.refreshTableModel();
    }

    private void boldingTheHeaders() {
        final JTableHeader jTableHeader = table.getTableHeader();
        jTableHeader.setFont(new Font("Serif", Font.BOLD, 15));
        table.setTableHeader(jTableHeader);
    }

    public void initializeEvents() {
        assert backButton != null && addButton != null && updateButton != null && removeButton != null;

        table.getSelectionModel().addListSelectionListener(this);
        backButton.addActionListener(this);
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        removeButton.addActionListener(this);
    }

    public JPanel getMedicinePanel() {
        return medicinePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            App.getInstance().backToHome();
            return;
        }

        final String id = idField.getText().trim();
        if (e.getSource() == addButton) {
            medicineHelper.insertData(id, this.getOptions());
        } else if (e.getSource() == updateButton) {
            medicineHelper.updateData(id, this.getOptions());
        } else if (e.getSource() == removeButton) {
            medicineHelper.deleteDataById(id);
        }
        this.getAllData();
        this.refreshTableModel();
    }

    public void getAllData() {
        try {
            dataList.clear();
            final ResultSet data = medicineHelper.getAllData();
            while (data.next()) {
                final String[] row = new String[]{
                        data.getString("id"),
                        data.getString("nama"),
                        data.getString("stok"),
                        data.getString("harga")};
                dataList.add(row);
            }
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
    }

    public void refreshTableModel() {
        final Object[][] array2D = new Object[dataList.size()][];
        final DefaultTableModel model = new DefaultTableModel(dataList.toArray(array2D), columns);
        table.setModel(model);
    }

    public HashMap<String, Object> getOptions() {
        try {
            final HashMap<String, Object> options = new HashMap<>();
            options.put("nama", namaField.getText().trim());
            options.put("stok", Integer.parseInt(stockField.getText().trim()));
            options.put("harga", Integer.parseInt(hargaField.getText().trim()));
            return options;
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            JOptionPane.showMessageDialog(null, "Maaf terjadi kesalahan\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (table.getSelectedRow() < 0) {
            return;
        }
        final int row = table.getSelectedRow();
        final Object selectedId = table.getValueAt(row, 0);
        idField.setText(selectedId.toString());
        namaField.setText(dataList.get(row)[1]);
        stockField.setText(dataList.get(row)[2]);
        hargaField.setText(dataList.get(row)[3]);
    }
}
