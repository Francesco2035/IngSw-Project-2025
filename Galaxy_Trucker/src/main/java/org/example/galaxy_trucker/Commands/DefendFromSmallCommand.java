package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

public class DefendFromSmallCommand extends Command {


    private IntegerPair batteryComp;


    public DefendFromSmallCommand(IntegerPair batteryComp,String gameId, String playerId, int lv, String title) {
        super(gameId, playerId, lv, title);
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
