package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

public class Storage extends Component{



    @Override
    public void initType() {

    }

    @Override
    public void rotate(Boolean direction) {

    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }
}
