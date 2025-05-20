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
    private Stage primaryStage;
    private static GuiMain instance;

    private static GuiRoot guiRoot;


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
        guiRoot.setGuiMain(this);
//        guiRoot.setStage(primaryStage);
        guiRoot.goToFirstScene(primaryStage);

    }


    public static void main(String[] args) {
        launch();
    }

    /**
     * Stops the application
     */
    public void stop () {
        Platform.exit();
        System.exit(0);
    }

    /**
     * @return A reference to the primary stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }


}