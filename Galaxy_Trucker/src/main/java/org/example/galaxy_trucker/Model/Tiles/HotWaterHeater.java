package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

public class HotWaterHeater extends Component{


    //per info su cannoni chiamare gettype


    private int EngineDirection = 3;
    public HotWaterHeater() {}



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
    public void insert(PlayerBoard playerBoard) {
        if (type == 2) {
            playerBoard.setEnginePower(2);
        }
        else  if (type == 1){
            playerBoard.setEnginePower(1);
        }
        playerBoard.getHotWaterHeaters().add(this);
    }




    @Override
    public void remove(PlayerBoard playerBoard) {
        if (type == 2) {
            playerBoard.setEnginePower(-2);
        }
        else if (type == 1){
            playerBoard.setEnginePower(-1);
        }
        playerBoard.getHotWaterHeaters().remove(this);
    }




    @Override
    public void setType(int type){
    }

}

