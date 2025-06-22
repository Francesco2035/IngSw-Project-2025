package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.PlasmaDrill;

public class GetPlasmaDrillPower extends ComponentAction {
    private double power;
    private int countDoublePlasmaDrills = 0;
    public GetPlasmaDrillPower(double SinglePower) {
        power = SinglePower;
    }

    @Override
    public void visit(PlasmaDrill plasmaDrill, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("You are not allowed to perform this action in this state");
        }
        double temp = plasmaDrill.getCannonPower();
        if (temp != 0){
            countDoublePlasmaDrills++;
        }
        power += plasmaDrill.getCannonPower();
    }

    public double getPower(){
        return power;
    }

    public int getCountDoublePlasmaDrills(){
        return countDoublePlasmaDrills;
    }

}
