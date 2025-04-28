package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

public class DefendFromLargeCommand extends Command{

    private IntegerPair plasmaDrill;
    private IntegerPair batteryComp;


    public DefendFromLargeCommand(IntegerPair plasmaDrill, IntegerPair batteryComp,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token);
        this.plasmaDrill = plasmaDrill;
        this.batteryComp = batteryComp;
    }


    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    @Override
    public void execute(Player player) {
        player.getCurrentCard().DefendFromLarge(plasmaDrill, batteryComp);
    }
}
