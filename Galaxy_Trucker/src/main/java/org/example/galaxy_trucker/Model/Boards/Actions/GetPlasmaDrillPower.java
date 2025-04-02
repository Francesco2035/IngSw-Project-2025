package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.PlasmaDrill;

public class GetPlasmaDrillPower extends ComponentActionVisitor{
    private double power;
    private int countDoublePlasmaDrills = 0;
    public GetPlasmaDrillPower(double SinglePower) {
        power = SinglePower;
    }

    @Override
    public void visit(PlasmaDrill plasmaDrill, PlayerStates state){
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
