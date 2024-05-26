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
    private JButton paymentButton;

    private BookingContainer bookingContainer;
    private ExaminationContainer examinationContainer;
    private PaymentContainer paymentContainer;

    public AppContainer() {
        initializeEvents();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    private void initializeEvents() {
        assert  this.bookingButton != null &&
                this.paymentButton != null &&
                this.examinationButton != null;
        this.examinationButton.addActionListener(this);
        this.bookingButton.addActionListener(this);
        this.paymentButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookingButton) {
            this.selectFrame(MenuType.BOOKING);
            logln("Select Booking Frame", LoggingType.DEBUG);
        } else if (e.getSource() == examinationButton) {
            this.selectFrame(MenuType.EXAMINATION);
            logln("Select Examination Frame", LoggingType.DEBUG);
        } else if (e.getSource() == paymentButton) {
            this.selectFrame(MenuType.PAYMENT);
            logln("Select Payment Frame", LoggingType.DEBUG);
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
            case PAYMENT:
                if (paymentContainer == null) {
                    paymentContainer = new PaymentContainer();
                }
                App.getInstance().setContentPane(paymentContainer.getPaymentPanel());
                break;
        }
        App.getInstance().pack();
        App.getInstance().setSize(new Dimension(800, 600));
        App.getInstance().setLocationRelativeTo(null);
    }
}