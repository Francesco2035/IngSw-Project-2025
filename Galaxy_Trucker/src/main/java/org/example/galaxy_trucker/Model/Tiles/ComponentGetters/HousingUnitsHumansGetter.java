package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;


public class HousingUnitsHumansGetter implements ComponentGetter{
//mannaggia al cazz
    private HousingUnit housingUnit;
    public HousingUnitsHumansGetter(Component component){
        this.housingUnit = (HousingUnit) component;
    }

    @Override
    public Object get() {
        return housingUnit.get(new HousingUnitsHumansGetter(this.housingUnit));
    }
}
