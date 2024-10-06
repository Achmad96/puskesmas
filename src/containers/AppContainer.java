package src.containers;

import src.App;
import src.enums.MenuType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppContainer implements ActionListener {

    private JPanel mainPanel;
    private JButton bookingButton;
    private JButton examinationButton;
    private JButton paymentButton;
    private JButton patientButton;
    private JButton healthWorkerButton;
    private JButton medicineButton;

    private BookingContainer bookingContainer;
    private ExaminationContainer examinationContainer;
    private PaymentContainer paymentContainer;
    private PatientContainer patientContainer;
    private HealthWorkerContainer healthWorkerContainer;
    private MedicineContainer medicineContainer;

    public AppContainer() {
        initializeEvents();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    private void initializeEvents() {
        assert  this.bookingButton != null &&
                this.paymentButton != null &&
                this.examinationButton != null &&
                this.patientButton != null &&
                this.medicineButton != null &&
                this.healthWorkerButton != null;
        this.bookingButton.addActionListener(this);
        this.examinationButton.addActionListener(this);
        this.paymentButton.addActionListener(this);
        this.patientButton.addActionListener(this);
        this.medicineButton.addActionListener(this);
        this.healthWorkerButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookingButton) {
            this.selectFrame(MenuType.BOOKING);
            System.out.println("Select Booking Frame");
        } else if (e.getSource() == examinationButton) {
            this.selectFrame(MenuType.EXAMINATION);
            System.out.println("Select Examination Frame");
        } else if (e.getSource() == paymentButton) {
            this.selectFrame(MenuType.PAYMENT);
            System.out.println("Select Payment Frame");
        } else if (e.getSource() == patientButton) {
            this.selectFrame(MenuType.PATIENT);
            System.out.println("Select Patient Frame");
        } else if (e.getSource() == medicineButton) {
            this.selectFrame(MenuType.MEDICINE);
            System.out.println("Select Medicine Frame");
        } else if (e.getSource() == healthWorkerButton) {
            this.selectFrame(MenuType.HEALTH_WORKER);
            System.out.println("Select Health Worker Frame");
        }
    }

    public void selectFrame(MenuType menuType) {
        switch (menuType) {
            case BOOKING:
                if (bookingContainer == null) {
                    bookingContainer = new BookingContainer();
                }
                App.getInstance().setPage(bookingContainer.getBookingPanel());
                break;
            case EXAMINATION:
                if (examinationContainer == null) {
                    examinationContainer = new ExaminationContainer();
                }
                App.getInstance().setPage(examinationContainer.getExaminationPanel());
                break;
            case PAYMENT:
                if (paymentContainer == null) {
                    paymentContainer = new PaymentContainer();
                }
                App.getInstance().setPage(paymentContainer.getPaymentPanel());
                break;
            case PATIENT:
                if (patientContainer == null) {
                    patientContainer = new PatientContainer();
                }
                App.getInstance().setPage(patientContainer.getPatientPanel());
                break;
            case MEDICINE:
                if (medicineContainer == null) {
                    medicineContainer = new MedicineContainer();
                }
                App.getInstance().setPage(medicineContainer.getMedicinePanel());
                break;
            case HEALTH_WORKER:
                if (healthWorkerContainer == null) {
                    healthWorkerContainer = new HealthWorkerContainer();
                }
                App.getInstance().setPage(healthWorkerContainer.getHealthWorkerPanel());
                break;
        }
        App.getInstance().pack();
        App.getInstance().setSize(new Dimension(800, 600));
        App.getInstance().setLocationRelativeTo(null);
    }
}