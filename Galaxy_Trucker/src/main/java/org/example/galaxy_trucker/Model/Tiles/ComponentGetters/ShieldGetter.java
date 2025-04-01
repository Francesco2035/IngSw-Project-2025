package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.ShieldGenerator;

public class ShieldGetter implements ComponentGetter {


    private ShieldGenerator component;
    public ShieldGetter(Component component) {
        this.component = (ShieldGenerator) component;
    }

    @Override
    public Object get() {
        return component.getProtectedDirections();
    }
}
