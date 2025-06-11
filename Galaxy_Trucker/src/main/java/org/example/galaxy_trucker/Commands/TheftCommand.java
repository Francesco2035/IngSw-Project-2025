package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;
import java.io.Serializable;

public class TheftCommand extends Command implements Serializable {
    int position;
    IntegerPair coordinate;
    public TheftCommand(int position, IntegerPair coordinate,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.position = position;
        this.coordinate = coordinate;
    }
    @Override
    public void execute(Player player) throws IOException {
        PlayerBoard playerBoard = player.getmyPlayerBoard();
        GetGoodAction action = new GetGoodAction(position,playerBoard,coordinate.getFirst(),coordinate.getSecond());
        playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent()
                , action, player.getPlayerState());
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}
