package src;

import src.containers.AppContainer;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

    public static App app;

    private final AppContainer appContainer;
    private final static int WIDTH = 900;
    private final static int HEIGHT = 600;
    private final static String title = "Puskesmas";
    private final static Dimension size = new Dimension(WIDTH, HEIGHT);

    private App () {
        appContainer = new AppContainer();
        this.setTitle(title);
        this.setContentPane(appContainer.getPanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();


        this.setSize(size);
        this.setMinimumSize(size);

        this.setLocationRelativeTo(null); // CENTER THE FRAME
        this.setVisible(true);
    }

    public void setPage(Container page) {
        this.setContentPane(page);
        this.setMinimumSize(size);
        this.setSize(size);
        this.setLocationRelativeTo(null);
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
