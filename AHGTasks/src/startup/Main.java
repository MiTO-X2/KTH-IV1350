package startup;

import controller.Controller;
import view.View;

/**
 * The entry point of the application.
 * This class is responsible for initializing the application by creating instances of
 * the Controller and View classes, which handle the application logic and user interface respectively.
 */
public class Main {

    /**
     * Main method that serves as the starting point of the application.
     * It creates a new Controller instance and passes it to the View.
     * This starts the application's flow by setting up the necessary components
     * for the user interface and the business logic.
     *
     * @param args Command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        Controller ctrl = new Controller();
        new View(ctrl);
    }
}