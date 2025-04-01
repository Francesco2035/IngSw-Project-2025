package org.example.galaxy_trucker.Model.Tiles.ComponentSetters;

import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.ShieldGenerator;

import java.util.Collections;

public class ShieldSetter implements ComponentSetter {


    private ShieldGenerator component;
    private boolean direction;
    public ShieldSetter(Component component, boolean direction) {
        this.component = (ShieldGenerator) component;
        this.direction = direction;
    }


    @Override
    public void set() {
        if (direction){
            Collections.rotate(component.getProtectedDirections(), 1);}
        else {Collections.rotate(component.getProtectedDirections(), -1);}

    }
}
