package org.example.galaxy_trucker;

import org.example.galaxy_trucker.PlayerPlance;

public abstract class Component {

    PlayerPlance myPlance;

    public Component() {;
    }

    public void referencePlance(PlayerPlance myPlance) {
        this.myPlance = myPlance;
    }

    public int getAbility(){
        return 0;
    }

    public int setAbility(){
        return 0;
    }

}
