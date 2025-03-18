package org.example.galaxy_trucker.Model.Tiles;

import java.util.ArrayList;
import java.util.Collections;

public class shieldGenerator extends Component{


    private ArrayList<Integer> protectedDirections;

    public shieldGenerator() {
    }

    @Override
    public void initType() {
        if (type.equals("nord-est")) {
            protectedDirections = new ArrayList<Integer>();
            protectedDirections.add(0);
            protectedDirections.add(1);
            protectedDirections.add(1);
            protectedDirections.add(0);
        }
    }


    @Override
    public ArrayList<Integer> getAbility(int integer) {
        return this.protectedDirections;
    }

    @Override
    public void setAbility(boolean direzione){
        if (direzione){Collections.rotate(this.protectedDirections, 1);}
        else {Collections.rotate(this.protectedDirections, -1);}
    }



}


