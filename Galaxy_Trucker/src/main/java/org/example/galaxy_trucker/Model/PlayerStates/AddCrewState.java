package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.AddCrewCommand;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;

public class AddCrewState extends PlayerState {


        @Override
        public boolean allows(AddCrewCommand command){
            return true;
        }

        @Override
        public boolean allows(AddCrewAction action) {
            return true;
        }
}
