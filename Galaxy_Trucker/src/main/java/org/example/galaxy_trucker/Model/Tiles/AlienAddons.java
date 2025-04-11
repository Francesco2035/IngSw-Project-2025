package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.PlayerStatesss;

public class AlienAddons extends Component{


//purple=true, brown=false

    public AlienAddons() {}

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void rotate(Boolean direction){}

    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        playerBoard.getAlienAddons().add(this);
    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getAlienAddons().remove(this);
    }

    @Override
    public Component clone(){
        AlienAddons clone = new AlienAddons();
        clone.type = type;
        return clone;
    }


}
