package org.example.galaxy_trucker;

import org.example.galaxy_trucker.PlayerPlance;

public abstract class Component {

    Tile[][] myPlance;

    public Component(PlayerPlance myPlance) {
        this.myPlance = myPlance.getPlayerPlance();
    }

    public int getAbility(){
        return 0;
    }

    public int setAbility(){
        return 0;
    }

}
