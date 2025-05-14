package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.FinishBuildingCommand;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;

public class ChoosePosition extends PlayerState {

    @Override
    public boolean allows(FinishBuildingCommand command){return true;}

    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        System.out.println("CHOOSEPOSITION");
        return new Command() {
            @Override
            public void execute(Player player) throws IOException {
                GameBoard board = player.getCommonBoard();
                board.SetStartingPosition(player);
                player.SetReady(true);

            }
        };
    }

}
