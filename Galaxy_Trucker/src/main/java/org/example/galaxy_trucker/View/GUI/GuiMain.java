package org.example.galaxy_trucker.View.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.galaxy_trucker.View.GUI.GuiRoot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class that deploys the GUI
 */
public class GuiMain extends Application {
    /**
     * The primary stage of the application where the GUI is displayed.
     * This is the main window of the application, which serves as the
     * container for various scenes and handles GUI-related interactions.
     */
    private Stage primaryStage;
    /**
     * Static instance of the GuiMain class.
     * Used to provide a global access point to the active instance of the GUI.
     */
    private static GuiMain instance;

    /**
     * A static reference to the root of the graphical user interface (GUI).
     * This variable is used to hold the instance of the GuiRoot class,
     * which serves as the entry point and main controller for setting up
     * and managing the GUI components of the application.
     *
     * The variable is initialized when {@code launchApp(GuiRoot guiRootInstance)}
     * is called. It allows access to GUI-related functionalities through
     * the GuiRoot instance, such as configuring stages and scenes.
     */
    private static GuiRoot guiRoot;

    /**
     * Constructor for the GuiMain class.
     * Initializes the instance of the GuiMain application.
     */
    public GuiMain() {
        instance = this;
    }

    /**
     * Launches the application
     * @param guiRootInstance sets the GUI root
     */
    public static void launchApp(GuiRoot guiRootInstance) {
        guiRoot = guiRootInstance;
        Application.launch(GuiMain.class);
    }


    /**
     * Starts the application and displays first scene
     * @param stage Stage to set
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        guiRoot.setStage(primaryStage);
        guiRoot.sceneSetup();
    }


    /**
     * The main entry point of the application.
     * This method initializes and launches the application.
     *
     * @param args Command-line arguments passed to the application, if any.
     */
    public static void main(String[] args) {
        launch();
    }

}