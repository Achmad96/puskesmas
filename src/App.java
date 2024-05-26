package src;

import src.containers.AppContainer;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

    public static App app;
    private final AppContainer appContainer;

    public App () {
        appContainer = new AppContainer();
        this.setTitle("Puskesmas");
        this.setContentPane(appContainer.getPanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        this.setSize(new Dimension(800,600));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void backToHome() {
        this.setContentPane(appContainer.getPanel());
    }

    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::getInstance);
    }
}
