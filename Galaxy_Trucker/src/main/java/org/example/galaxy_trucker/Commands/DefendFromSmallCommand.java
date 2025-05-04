package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

public class DefendFromSmallCommand extends Command implements Serializable {

    @JsonProperty("commandType")
    private final String commandType = "DefendFromSmallCommand";

    private IntegerPair batteryComp;


    public DefendFromSmallCommand(IntegerPair batteryComp,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token);
        this.batteryComp = batteryComp;
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    @Override
    public void execute(Player player) {
        player.getCurrentCard().DefendFromSmall(batteryComp);
    }
}
