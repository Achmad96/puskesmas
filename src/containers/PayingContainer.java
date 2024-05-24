package src.containers;

import src.App;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PayingContainer implements ActionListener {

    private JPanel PayingPanel;
    private JTable table1;
    private JTextField textField1;
    private JTextField textField2;
    private JPasswordField passwordField1;
    private JTextField textField3;
    private JTextField textField4;
    private JButton findButton;
    private JButton addButton;
    private JButton backButton;

    public JPanel getPayingPanel() {
        return PayingPanel;
    }
    public PayingContainer() {
        this.initializeEvents();
    }
    public void initializeEvents() {
        assert  backButton != null &&
                addButton != null &&
                findButton != null;
        backButton.addActionListener(this);
        addButton.addActionListener(this);
        findButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            App.getInstance().backToHome();
        } else if (e.getSource() == addButton) {

        }
    }
}
