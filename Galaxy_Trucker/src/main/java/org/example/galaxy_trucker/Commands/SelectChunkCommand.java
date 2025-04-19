package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

public class SelectChunkCommand extends Command {


    IntegerPair chunk;

    public SelectChunkCommand(IntegerPair chunk,String gameId, String playerId, int lv, String title) {
        super(gameId, playerId, lv, title);
        this.chunk = chunk;
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    @Override
    public void execute(Player player) {
        PlayerBoard playerBoard = player.getmyPlayerBoard();
        playerBoard.modifyPlayerBoard(playerBoard.choosePlayerBoard(chunk));
        player.getCurrentCard().keepGoing();
    }
}
