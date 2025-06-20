package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Commands.RemoveTileCommand;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.BaseStateClient;
import org.example.galaxy_trucker.View.ClientModel.States.CheckValidityClient;

import java.io.IOException;

public class CheckValidity extends PlayerState{


    @Override
    public boolean allows(RemoveTileCommand command) {
        return true;
    }

    @Override
    public boolean allows(ReadyCommand command) {
        return true;
    }

    @Override
    public Command createDefaultCommand(String gameId,Player player) {

        return new Command() {
            @Override
            public void execute(Player player) throws IOException {
                while (!player.getmyPlayerBoard().checkValidity()){
                    for (int i = 0; i < 10 ; i ++){
                        for (int j = 0; j < 10 ; j ++){
                            if (player.getmyPlayerBoard().getToRemovePB()[i][j] == -2){
                                player.getmyPlayerBoard().removeTile(i, j);
                            }
                        }
                    }
                }
                player.SetReady(true);
            }
        };
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new CheckValidityClient());
    }

}
