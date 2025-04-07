package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.HotWaterHeater;


public class GetEnginePower extends ComponentAction {
    private int power;
    private int countDoubleEngine = 0;
    public GetEnginePower(int singlePower) {
        power = singlePower;
    }

    @Override
    public void visit(HotWaterHeater hotWaterHeater, PlayerState playerState) {

        if (!playerState.allows(this)){ //overkill gay
            throw new IllegalStateException("illegal state");
        }
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
