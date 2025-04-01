package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.PlasmaDrill;

public class CannonPowerGetter implements ComponentGetter{


    private PlasmaDrill component;

    public CannonPowerGetter(Component component) {
        this.component = ((PlasmaDrill) component);
    }

    @Override
    public Object get() {
        if (component.isDouble()) {return 2;}
        else {return 1;}
    }
}
