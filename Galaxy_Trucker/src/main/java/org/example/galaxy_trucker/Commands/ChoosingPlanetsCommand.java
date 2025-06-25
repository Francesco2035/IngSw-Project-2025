package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

public class ChoosingPlanetsCommand extends Command implements Serializable {

    @JsonProperty("planet")
    int planet;

    @JsonProperty("commandType")
    private final String commandType = "ChoosingPlanetsCommand";

    public ChoosingPlanetsCommand() {}

    public ChoosingPlanetsCommand(int planet , String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
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
