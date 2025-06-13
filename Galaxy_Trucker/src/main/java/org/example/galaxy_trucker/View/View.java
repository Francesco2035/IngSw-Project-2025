package org.example.galaxy_trucker.View;

import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.*;

import java.io.IOException;

public interface View {

    // Metodo per aggiornare la board con un nuovo TileEvent
    void updateBoard(TileEvent tileEvent);


    // Metodo per mostrare un messaggio alla vista (per esempio, per notifiche o errori)
    void showMessage(String message);

    // Metodo per chiedere input all'utente, se necessario
    String askInput(String message);

    void updateHand(HandEvent event);

    void updateGameboard(GameBoardEvent event);

    void updateCoveredTilesSet(CoveredTileSetEvent event);

    void updateUncoveredTilesSet(UncoverdTileSetEvent event);

    void showDeck(DeckEvent event);

//    void updateGameBoard(GameBoardEvent event);

    void showCard(CardEvent event);

    void disconnect();

    void connect() throws IOException;

    public void setGameboard(int lv);

    void showLobby(LobbyEvent event);

    void showLobbyGame(GameLobbyEvent event);

    void rewardsChanged(RewardsEvent event);


    void phaseChanged(PhaseEvent event);

    void exceptionOccurred(ExceptionEvent exceptionEvent);

    void updateOthersPB(PlayerTileEvent playerTileEvent);

    void seeBoards();

    void refresh();

    void effectCard(LogEvent event);

    void updatePBInfo(PBInfoEvent event);

    void updateHourglass(HourglassEvent event);

    void seeLog();

    void showOutcome(FinishGameEvent event);
}
