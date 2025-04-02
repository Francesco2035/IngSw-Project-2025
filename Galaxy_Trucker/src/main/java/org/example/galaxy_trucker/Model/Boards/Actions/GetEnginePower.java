package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.HotWaterHeater;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;

public class GetEnginePower extends ComponentActionVisitor{
    private int power = 0;

    @Override
    public void visit(HotWaterHeater hotWaterHeater, PlayerStates State) {
        power+= hotWaterHeater.getEnginePower();
    }

    public  int getPower(){
        return power;
    }
}
