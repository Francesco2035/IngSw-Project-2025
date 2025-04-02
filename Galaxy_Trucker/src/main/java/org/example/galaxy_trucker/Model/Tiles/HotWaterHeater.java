package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Actions.ComponentActionVisitor;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;

public class HotWaterHeater extends Component{

    private boolean isDouble;

    private int EngineDirection = 3;

    public HotWaterHeater() {}

    public boolean isDouble() {
        return isDouble;
    }

    public void setDouble(boolean aDouble) {
        isDouble = aDouble;
    }


    @Override
    public void initType(){
        if (type==(1)) isDouble = false;
        else if (type==(2)) isDouble = true;
    }

    @Override
    public void rotate(Boolean direction) {
        if (direction){
            EngineDirection += 1;
            EngineDirection = EngineDirection % 4;
        }
        else {
            EngineDirection -= 1;
            EngineDirection = EngineDirection % 4;
        }
    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y){
        return EngineDirection == 3;
    }


    @Override
    public void accept(ComponentActionVisitor visitor, PlayerStates State) {
        if (!State.equals(PlayerStates.GiveSpeed)){
            throw new IllegalStateException("invalid state");
        }
        visitor.visit(this, State);
    }

    @Override
    public void insert(PlayerBoard playerBoard) {
        if (type == 1) {
            playerBoard.setEnginePower(1);
        }

        playerBoard.getHotWaterHeaters().add(this);
    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        if (type == 1) {
            playerBoard.setEnginePower(-1);
        }

        playerBoard.getHotWaterHeaters().remove(this);
    }


    public int getEnginePower(){
        if (type == 2){
            return 2;
        }
        else {
            return 0;
        }
    }

}

