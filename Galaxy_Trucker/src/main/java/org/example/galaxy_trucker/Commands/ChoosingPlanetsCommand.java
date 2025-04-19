package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

public class ChoosingPlanetsCommand extends Command{

    int planet;



    public ChoosingPlanetsCommand(int planet , String gameId, String playerId, int lv, String title) {
        super(gameId, playerId, lv, title);
        this.planet = planet;
    }

    @Override
    public void execute(Player player) {

        player.getCurrentCard().choosePlanet(planet);

    }
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}
