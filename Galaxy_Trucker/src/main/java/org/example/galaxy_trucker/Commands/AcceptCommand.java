package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

public class AcceptCommand extends Command {

    boolean accepting;


    public AcceptCommand(String gameId, String playerId, int lv, String title,boolean accepting) {
        super(gameId, playerId, lv, title);
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
