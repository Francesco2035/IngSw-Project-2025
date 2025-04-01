package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.*;

public class NearAddonsGetter implements ComponentGetter{

    private boolean type;
    private ModularHousingUnit component;

    public NearAddonsGetter(Component component, boolean type){
        this.component = (ModularHousingUnit) component;
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
