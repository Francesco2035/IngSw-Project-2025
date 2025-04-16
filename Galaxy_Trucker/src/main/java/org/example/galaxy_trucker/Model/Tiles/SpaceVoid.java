package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;


public class SpaceVoid extends Component{


    @Override
    public void rotate(Boolean direction) {throw new InvalidInput("you can't rotate spaceVoid tile");}


    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {

    }

    @Override
    public void remove(PlayerBoard playerBoard) {}

    @Override
    public Component clone(){
        return new SpaceVoid();
    }



}

