package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Tiles.ComponentCheckers.PlasmaDrillsChecker;

public class PlasmaDrill extends Component{


    private boolean isDouble;

    public PlasmaDrill() {}
    public boolean isDouble() {
        return isDouble;
    }
    public void setDouble(boolean aDouble) {
        isDouble = aDouble;
    }


    @Override
    public void initType(){
        if (type==1) isDouble = false;
        else if (type==2) isDouble = true;
    }

    @Override
    public void rotate(Boolean direction) {}


//
//
    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y){
        setComponentChecker(new PlasmaDrillsChecker(pb,x,y));
        return getComponentChecker().Check();
    };





}
