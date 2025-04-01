package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;

import java.util.ArrayList;

public class HousingHumanGetter implements ComponentGetter{


    private HousingUnit component;
    public HousingHumanGetter(Component component) {
        this.component = ((HousingUnit) component);
    }

    @Override
    public Object get() {
        return component.getNumHumans();
    }
}
