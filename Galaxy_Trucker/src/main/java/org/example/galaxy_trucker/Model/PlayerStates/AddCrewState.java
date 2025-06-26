package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.AddCrewCommand;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.View.ClientModel.States.AddCrewClient;

import java.io.IOException;
import java.util.ArrayList;

public class AddCrewState extends PlayerState {


        @Override
        public boolean allows(AddCrewCommand command){
            return true;
        }

        @Override
        public boolean allows(AddCrewAction action) {
            return true;
        }

    @Override
    public Command createDefaultCommand(String gameId,Player player) {
        return new Command() {
            @Override
            public void execute(Player player) throws IOException {
                System.out.println("DEF ADD CREW");
                ArrayList<HousingUnit> units = player.getmyPlayerBoard().getHousingUnits();
                for (HousingUnit unit : units) {
                    if(unit.getNumHumans() == 0 && !unit.isPurpleAlien() && !unit.isBrownAlien()) {
                        player.getmyPlayerBoard().performAction(unit, new AddCrewAction(2, false, false, player.getmyPlayerBoard()), player.getPlayerState());
                    }
                }
                player.SetReady(true);

            }
        };
    }


    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new AddCrewClient());
    }
}
