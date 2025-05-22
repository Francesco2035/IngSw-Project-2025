package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.io.Serializable;

public class TheftCommand extends Command implements Serializable {
    int position;
    IntegerPair coordinate;
    public TheftCommand(int position, IntegerPair coordinate) {
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
}
