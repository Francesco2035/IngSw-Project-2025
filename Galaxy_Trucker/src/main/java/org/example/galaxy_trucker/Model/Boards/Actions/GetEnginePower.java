package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.HotWaterHeater;


public class GetEnginePower extends ComponentActionVisitor{
    private int power;
    private int countDoubleEngine = 0;
    public GetEnginePower(int singlePower) {
        power = singlePower;
    }

    @Override
    public void visit(HotWaterHeater hotWaterHeater, PlayerStates State) {
        int temp = hotWaterHeater.getEnginePower();
        if (temp != 0) {
            countDoubleEngine++;
        }
        power+= temp;
    }

    public  int getPower(){
        return power;
    }

    public int getCountDoubleEngine(){
        return countDoubleEngine;
    }

}
