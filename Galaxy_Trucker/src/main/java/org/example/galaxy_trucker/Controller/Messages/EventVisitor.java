package org.example.galaxy_trucker.Controller.Messages;

import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.*;

public interface EventVisitor {



    public void visit(DeckEvent event);

    public void visit(CardEvent event);

    void visit(GameLobbyEvent event);

    public void visit(HandEvent event);

    public void visit(VoidEvent event);

    public void visit(TileEvent event);

    public void visit(UncoverdTileSetEvent event);

    public void visit(CoveredTileSetEvent event);

    public void visit(GameBoardEvent gameBoardEvent);

    void visit(LobbyEvent lobbyEvent);

    void visit(PhaseEvent phaseEvent);

    void visit(RewardsEvent rewardsEvent);

    void visit(ExceptionEvent exceptionEvent);

    void visit(PlayerTileEvent playerTileEvent);

    void visit(RandomCardEffectEvent randomCardEffectEvent);

    void visit(ConnectionRefusedEvent connectionRefusedEvent);


    //public void visit(GameBoardEvent event);
}
