package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.modularHousingUnit;

public class HousingUnitsHumansGetter implements ComponentGetter{
//mannaggia al cazz
    private modularHousingUnit housingUnit;
    public HousingUnitsHumansGetter(modularHousingUnit component){
        this.housingUnit = component;
    }

    @Override
    public Object get() {
        return housingUnit.getAbility();
    }
}
