package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

public class ReadyCommand extends Command implements Serializable {


    @JsonProperty("commandType")
    private final String commandType = "ReadyCommand";

    @JsonProperty("ready")
    boolean ready;

    public ReadyCommand(String gameId, String playerId, int lv, String title, boolean ready) {
        super(gameId, playerId, lv, title);
        this.ready = ready;
    }

    @Override
    public void execute(Player player) {
        System.out.print("execute.... ");
        switch (title){
            case "Quit": {
             player.getCommonBoard().abandonRace(player);
             break;
            }
            case "Ready": {
                player.SetReady(ready);
                System.out.println("Ready state set to " + ready);
                break;
            }
            default: {
                throw new InvalidInput("invalid Title");
            }

        }
    }

    public ReadyCommand() {

    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}
