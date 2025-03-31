package org.example.galaxy_trucker.Model.Tiles.ComponentSetters;

import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;

public class HousingSetter implements ComponentSetter{




    private int numHumans;
    private boolean purpleAlien, brownAlien;
    private HousingUnit component;
    public HousingSetter(Component component, int numHumans, boolean purpleAlien, boolean brownAlien) {
        this.numHumans = numHumans;
        this.purpleAlien = purpleAlien;
        this.brownAlien = brownAlien;
        this.component = (HousingUnit) component;
    }


    @Override
    public void set() {
        this.component.setNumHumans(this.numHumans);
        this.component.setPurpleAlien(this.purpleAlien);
        this.component.setBrownAlien(this.brownAlien);
    }
}
