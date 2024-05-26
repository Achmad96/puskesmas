package src.containers;

import src.App;
import src.enums.LoggingType;
import src.enums.MenuType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static src.utils.LoggingUtil.logln;

public class AppContainer implements ActionListener {

    private JPanel mainPanel;
    private JButton examinationButton;
    private JButton bookingButton;
    private JButton payingButton;

    private BookingContainer bookingContainer;
    private ExaminationContainer examinationContainer;
    private PayingContainer payingContainer;

    public AppContainer() {
        initializeEvents();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    private void initializeEvents() {
        assert  this.bookingButton != null &&
                this.payingButton != null &&
                this.examinationButton != null;
        this.examinationButton.addActionListener(this);
        this.bookingButton.addActionListener(this);
        this.payingButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookingButton) {
            this.selectFrame(MenuType.BOOKING);
            logln("Select Booking Frame", LoggingType.DEBUG);
        } else if (e.getSource() == examinationButton) {
            this.selectFrame(MenuType.EXAMINATION);
            logln("Select Examination Frame", LoggingType.DEBUG);
        } else if (e.getSource() == payingButton) {
            this.selectFrame(MenuType.PAYING);
            logln("Select Paying Frame", LoggingType.DEBUG);
        }
    }

    public void selectFrame(MenuType menuType) {
        switch (menuType) {
            case BOOKING:
                if (bookingContainer == null) {
                    bookingContainer = new BookingContainer();
                }
                App.getInstance().setContentPane(bookingContainer.getBookingPanel());
                break;
            case EXAMINATION:
                if (examinationContainer == null) {
                    examinationContainer = new ExaminationContainer();
                }
                App.getInstance().setContentPane(examinationContainer.getExaminationPanel());
                break;
            case PAYING:
                if (payingContainer == null) {
                    payingContainer = new PayingContainer();
                }
                App.getInstance().setContentPane(payingContainer.getPayingPanel());
                break;
        }
        App.getInstance().pack();
        App.getInstance().setSize(new Dimension(800, 600));
        App.getInstance().setLocationRelativeTo(null);
    }
}