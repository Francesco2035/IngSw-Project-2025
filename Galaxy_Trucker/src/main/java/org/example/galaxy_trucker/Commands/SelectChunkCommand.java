package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

public class SelectChunkCommand extends Command implements Serializable {


    IntegerPair chunk;

    public SelectChunkCommand(IntegerPair chunk,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
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

        try {
            player.getCurrentCard().keepGoing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
