package org.example.galaxy_trucker;

import org.example.galaxy_trucker.PlayerPlance;

public abstract class Component {

    PlayerPlance plance;

    private Component(PlayerPlance plance) {
        this.plance = plance.getPlayerPlance();
    };
}
