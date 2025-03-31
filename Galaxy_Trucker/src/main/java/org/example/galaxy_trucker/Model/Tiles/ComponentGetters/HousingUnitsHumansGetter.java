package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;


public class HousingUnitsHumansGetter implements ComponentGetter{
//mannaggia al cazz
    private ModularHousingUnit housingUnit;
    public HousingUnitsHumansGetter(ModularHousingUnit component){
        this.housingUnit = component;
    }

    @Override
    public Object get() {
        return housingUnit.get(new HousingUnitsHumansGetter(this.housingUnit));
    }
}
