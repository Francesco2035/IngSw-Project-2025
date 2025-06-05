package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;
import java.util.ArrayList;

public class ConsumeEnergyCommand extends Command implements Serializable {

    private ArrayList<IntegerPair> coordinate;

    public ConsumeEnergyCommand(ArrayList<IntegerPair> coordinate,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.coordinate = coordinate;
    }

    @Override
    public void execute(Player player) {
        player.getCurrentCard().consumeEnergy(coordinate);
    }


    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}
