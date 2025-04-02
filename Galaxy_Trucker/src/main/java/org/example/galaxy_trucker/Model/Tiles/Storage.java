package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Actions.ComponentActionVisitor;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;

public class Storage extends Component{



    @Override
    public void initType() {

    }

    @Override
    public void rotate(Boolean direction) {

    }

    @Override
    public void accept(ComponentActionVisitor visitor, PlayerStates State) {

    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }


}
