package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.HotWaterHeater;

public class EnginePowerGetter implements ComponentGetter{


    private HotWaterHeater component;

    public EnginePowerGetter(Component component) {
        this.component = ((HotWaterHeater) component);
    }

    @Override
    public Object get() {
        if (component.isDouble()) {return 2;}
        else {return 1;}
    }
}
