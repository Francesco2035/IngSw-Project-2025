package org.example.galaxy_trucker.View.GUI;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiOut {

    private GuiRoot gui;
    private Scene TitleScene;
    private Scene LobbyScene;
    private Scene GameLobbyScene;
    private Scene buildingScene;
    private Scene validityScene;
    private Scene addCrewScene;

    private Stage primaryStage;


    public GuiOut(GuiRoot root){
        gui = root;
    }

    public GuiRoot getRoot(){
        return gui;
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void setTitleScreen(Scene titleScene) {
        this.TitleScene=titleScene;
    }

    public void printTitleScreen(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Galaxy Truckers");
            primaryStage.setScene(TitleScene);
            primaryStage.show();
        });
    }



    public void setLobby(Scene lobbyScene){
        this.LobbyScene=lobbyScene;
    }

    public void printLobby(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Lobby");
            primaryStage.setScene(LobbyScene);
            primaryStage.show();
        });
    }


    public void setGameLobby(Scene gameLobbyScene){
        this.GameLobbyScene = gameLobbyScene;
    }

    public void printGameLobby(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Game Lobby");
            primaryStage.setScene(GameLobbyScene);
            primaryStage.show();
        });
    }


    public void setBuildingScene(Scene buildingScene){
        this.buildingScene=buildingScene;
    }

    public void printBuildingScreen(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Building Screen");
            primaryStage.setScene(buildingScene);
            primaryStage.show();
        });
    }

    public void setAddCrewScreen(Scene addCrewScene){
        this.addCrewScene=addCrewScene;
    }
    public void printAddCrewScreen(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Add Crew");
            primaryStage.setScene(addCrewScene);
            primaryStage.show();
        });
    }

    public void setCheckValidityScreen(Scene scene){
        this.validityScene= scene;
    }
    public void printCheckValidityScreen(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Check Validity");
            primaryStage.setScene(validityScene);
            primaryStage.show();
        });
    }

}
