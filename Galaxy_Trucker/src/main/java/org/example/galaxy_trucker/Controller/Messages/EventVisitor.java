package org.example.galaxy_trucker.Controller.Messages;

import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.*;

public interface EventVisitor {



    public void visit(DeckEvent event);

    public void visit(CardEvent event);

    public void visit(HandEvent event);

    public void visit(VoidEvent event);

    public void visit(TileEvent event);

    public void visit(UncoverdTileSetEvent event);

    public void visit(CoveredTileSetEvent event);

    public void visit(GameBoardEvent gameBoardEvent);

    void visit(LobbyEvent lobbyEvent);

    //public void visit(GameBoardEvent event);
}
