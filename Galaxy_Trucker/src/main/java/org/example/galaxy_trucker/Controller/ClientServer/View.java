package org.example.galaxy_trucker.Controller.ClientServer;

import org.example.galaxy_trucker.Controller.Messages.GameBoardEvent;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.VoidEvent;

import java.util.ArrayList;

public interface View {

    // Metodo per aggiornare la board con un nuovo TileEvent
    void updateBoard(TileEvent tileEvent);


    // Metodo per mostrare un messaggio alla vista (per esempio, per notifiche o errori)
    void showMessage(String message);

    // Metodo per chiedere input all'utente, se necessario
    String askInput(String message);

    void updateHand(HandEvent event);

    void updateGameboard();

    void updateCoveredTilesSet(CoveredTileSetEvent event);

    void updateUncoveredTilesSet(UncoverdTileSetEvent event);

    void showDeck(DeckEvent event);

    void updateGameBoard(GameBoardEvent event);

    void showCard(int id);

    void disconnect();

    void connect();
}
