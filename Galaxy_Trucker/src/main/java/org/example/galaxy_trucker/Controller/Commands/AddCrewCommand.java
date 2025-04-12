package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

public class AddCrewCommand implements Command {

    int numHumans;
    boolean purpleAlien;
    boolean brownAlien;
    Player player;
    IntegerPair coordinate;

    public AddCrewCommand(int numHumans, boolean purpleAlien, boolean brownAlien, Player player, IntegerPair coordinate) {
        this.numHumans = numHumans;
        this.purpleAlien = purpleAlien;
        this.brownAlien = brownAlien;
        this.player = player;
        this.coordinate = coordinate;
    }

    @Override
    public void execute() {
        PlayerBoard playerBoard = player.getmyPlayerBoard();
        AddCrewAction action = new AddCrewAction(numHumans,purpleAlien,brownAlien, playerBoard);
        playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent(),action, player.getPlayerState());
    }

}
