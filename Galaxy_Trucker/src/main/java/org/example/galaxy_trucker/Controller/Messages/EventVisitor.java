package org.example.galaxy_trucker.Controller.Messages;

import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.*;
//import org.example.galaxy_trucker.Messages.TileSets.*;

public interface EventVisitor {



    void visit(DeckEvent event);

    void visit(CardEvent event);

    void visit(GameLobbyEvent event);

    void visit(HandEvent event);

    void visit(VoidEvent event);

    void visit(TileEvent event);

    void visit(UncoverdTileSetEvent event);

    void visit(CoveredTileSetEvent event);

    void visit(GameBoardEvent gameBoardEvent);

    void visit(LobbyEvent lobbyEvent);

    void visit(PhaseEvent phaseEvent);

    void visit(RewardsEvent rewardsEvent);

    void visit(ExceptionEvent exceptionEvent);

    void visit(PlayerTileEvent playerTileEvent);

    void visit(LogEvent event);

    void visit(ConnectionRefusedEvent connectionRefusedEvent);

    void visit(PBInfoEvent pbInfoEvent);

    void visit(QuitEvent quitEvent);

    void visit(HourglassEvent hourglassEvent);

    void visit(FinishGameEvent finishGameEvent);

    void visit(ReconnectedEvent reconnectedEvent);

    void visit(TokenEvent tokenEvent);

    void visit(ScoreboardEvent scoreboardEvent);


    //public void visit(GameBoardEvent event);
}
