package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;

import java.util.ArrayList;

public class HousingHumanGetter implements ComponentGetter{


    private ModularHousingUnit component;
    public HousingHumanGetter(Component component) {
        this.component = ((ModularHousingUnit) component);
    }

    @Override
    public Object get() {
        return component.getNumHumans();
    }
}
