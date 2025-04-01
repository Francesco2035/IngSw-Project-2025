package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.AlienAddons;
import org.example.galaxy_trucker.Model.Tiles.Component;

public class AlienGetter implements ComponentGetter {


    private AlienAddons component;
    public AlienGetter(Component component) throws IllegalArgumentException {
        this.component = ((AlienAddons) component);
    }


    @Override
    public Object get() {
        return component.isWhatColor();
    }
}
