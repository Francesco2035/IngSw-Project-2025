package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

public class DefendFromLargeCommand extends Command implements Serializable {

    @JsonProperty("plasmaDrill")
    private IntegerPair plasmaDrill;
    @JsonProperty("batteryComp")
    private IntegerPair batteryComp;

    public DefendFromLargeCommand() {}

    public DefendFromLargeCommand(IntegerPair plasmaDrill, IntegerPair batteryComp,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.plasmaDrill = plasmaDrill;
        this.batteryComp = batteryComp;
    }


    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    @Override
    public void execute(Player player) {
        try {
            player.getCurrentCard().DefendFromLarge(plasmaDrill, batteryComp,player);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
