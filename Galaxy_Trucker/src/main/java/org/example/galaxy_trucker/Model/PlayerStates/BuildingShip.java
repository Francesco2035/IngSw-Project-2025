package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.BuildingClient;

public class BuildingShip extends PlayerState {


    @Override
    public boolean allows(BuildingCommand command) {
        return true;
    }

    @Override
    public boolean allows(ReadyCommand command) {
        return true;
    }

    @Override
    public boolean allows(FinishBuildingCommand command){return true;}

    @Override
    public Command createDefaultCommand(String gameId, Player player) { // dovrebbe andare bene?

        return new Command() {
            @Override
            public void execute(Player player) {
                System.out.println("FINISH BUILDING");

                try{
                    GameBoard board = player.getCommonBoard();
                    player.EndConstruction();

                }
                catch(Exception e){
                    e.printStackTrace();
                }
                player.SetReady(true);
                player.SetHasActed(true);
                System.out.println("FINISH BUILDING FINISHED");

            }
        };



    }


    @Override
    public void shouldAct(Player player) {
        player.SetHasActed(false);
    }
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new BuildingClient());
    }
}
