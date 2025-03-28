package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.*;
import org.example.galaxy_trucker.Model.Tiles.ComponentCheckers.ModularHousingUnitChecker;

public class NearAddonsGetter implements ComponentGetter{

    private boolean type;
    private modularHousingUnit component;
    public NearAddonsGetter(modularHousingUnit component, boolean type){
        this.component = component;
        this.type = type;
    }

    @Override
    public Object get(){
        if (type){
            return component.getNearPurple();
        }
        return component.getNearBrown();
    }

}
