package org.example.galaxy_trucker.Model.Tiles.ComponentSetters;

import org.example.galaxy_trucker.Model.Tiles.*;

public class EnergySetter implements ComponentSetter {


    private PowerCenter component;
    public EnergySetter(Component component) throws IllegalArgumentException {
        if(((PowerCenter) component).getEnergy() < 0) throw new IllegalArgumentException("Energy cannot be negative");
        this.component = ((PowerCenter) component);
    }

    @Override
    public void set() {
        if(component.getEnergy() == 0) throw new IllegalArgumentException("cannot exceed 0");
        else component.setEnergy(component.getEnergy()-1);
    }
}
