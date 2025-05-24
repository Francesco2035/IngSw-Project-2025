package org.example.galaxy_trucker.View.GUI;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiOut {

    private GuiRoot gui;
    Scene TitleScene;
    Scene LobbyScene;
    Scene GameLobbyScene;



    Stage primaryStage;

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
}
