package org.example.galaxy_trucker.View.GUI;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Controller.ClientServer.View;
import org.example.galaxy_trucker.Controller.Messages.GameBoardEvent;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;



public class GUI extends Application implements View {

    CommandInterpreter commandInterpreter;


    @Override
    public void updateBoard(TileEvent tileEvent) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public String askInput(String message) {
        return "";
    }

    @Override
    public void updateHand(HandEvent event) {

    }

    @Override
    public void updateGameboard(GameBoardEvent event) {

    }

    @Override
    public void updateCoveredTilesSet(CoveredTileSetEvent event) {

    }

    @Override
    public void updateUncoveredTilesSet(UncoverdTileSetEvent event) {

    }

    @Override
    public void showDeck(DeckEvent event) {

    }

    @Override
    public void showCard(int id) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void connect() {

    }

    @Override
    public void setGameboard(int lv) {

    }

    @Override
    public void start(Stage stage) throws Exception {

    }



}
