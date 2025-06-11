package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;

public class QuitCommand extends Command{


    public QuitCommand(String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token, -1);
    }


    public QuitCommand(){

    }

    @Override
    public void execute(Player player) throws IOException {

    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}
