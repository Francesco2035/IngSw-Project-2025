package org.example.galaxy_trucker.Model.Tiles.ComponentGetters;

import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;

public class HousingAlienGetter implements ComponentGetter {


    private boolean type;
    private HousingUnit component;

    public HousingAlienGetter(Component component, boolean type) {
        this.component = (HousingUnit) component;
        this.type = type;
    }

    @Override
    public Object get() {
        if (type) {
            return component.isPurpleAlien();
        }
        return component.isBrownAlien();
    }
}
