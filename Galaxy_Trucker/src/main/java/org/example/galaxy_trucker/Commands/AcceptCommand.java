package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

public class AcceptCommand extends Command implements Serializable {

    @JsonProperty("accepting")
    boolean accepting;

    public AcceptCommand() {}

    public AcceptCommand(String gameId, String playerId, int lv, String title,boolean accepting, String token) {
        super(gameId, playerId, lv, title, token, -1);
        this.accepting = accepting;
    }

    @Override
    public void execute(Player player) {
        player.getCurrentCard().continueCard(accepting);
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}
