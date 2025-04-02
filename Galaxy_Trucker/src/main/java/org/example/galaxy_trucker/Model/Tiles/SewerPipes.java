package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentActionVisitor;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates;

import java.util.ArrayList;
import java.util.HashMap;

public class SewerPipes extends Component {



    @Override
    public void initType() {}


    @Override
    public void rotate(Boolean direction) {

    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void accept(ComponentActionVisitor visitor, PlayerStates State) {
        throw new InvalidInput("cannot accept this action on SewerPipes tile: " +visitor.getClass().getSimpleName());
    }


}

