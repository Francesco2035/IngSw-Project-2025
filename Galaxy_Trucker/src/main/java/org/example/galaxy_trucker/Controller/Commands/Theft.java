package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

public class Theft implements Command {

    IntegerPair pair;
    Player player;
    int position;

    public Theft(int position,IntegerPair pair, Player player) {
        this.pair = pair;
        this.player = player;
        this.position = position;
    }

    @Override
    public void execute() {
        player.getCurrentCard().loseCargo(pair,position);
    }
}
