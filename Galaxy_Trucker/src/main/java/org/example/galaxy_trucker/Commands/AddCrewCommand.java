package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

public class AddCrewCommand extends Command implements Serializable {

    @JsonProperty("numHumans")
    int numHumans;
    @JsonProperty("purpleAlien")
    boolean purpleAlien;
    @JsonProperty("brownAlien")
    boolean brownAlien;
    @JsonProperty("coordinate")
    IntegerPair coordinate;

    @JsonProperty("commandType")
    private final String commandType = "AddCrewCommand";




    public AddCrewCommand(int numHumans, boolean purpleAlien, boolean brownAlien, IntegerPair coordinate, String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token);
        this.numHumans = numHumans;
        this.purpleAlien = purpleAlien;
        this.brownAlien = brownAlien;
        this.coordinate = coordinate;
    }

    @Override
    public void execute(Player player) {
        PlayerBoard playerBoard = player.getmyPlayerBoard();
        AddCrewAction action = new AddCrewAction(numHumans,purpleAlien,brownAlien, playerBoard);
        playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent(),action, player.getPlayerState());
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    public AddCrewCommand(){

    }

}
