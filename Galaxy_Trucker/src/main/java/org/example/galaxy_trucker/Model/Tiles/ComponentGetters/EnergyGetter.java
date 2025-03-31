package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;

public class EnergyGetter implements ComponentGetter {


    private PowerCenter component;
    public EnergyGetter(Component component) {
        this.component = ((PowerCenter) component);
    }


    @Override
    public Object get() {
        return component.getEnergy();
    }
}
