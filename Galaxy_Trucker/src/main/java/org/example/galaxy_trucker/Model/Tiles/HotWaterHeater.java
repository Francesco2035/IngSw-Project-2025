package org.example.galaxy_trucker.Model.Tiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.Actions.GetEnginePower;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.util.ArrayList;

public class HotWaterHeater extends Component{


    //per info su cannoni chiamare gettype


    private int EngineDirection = 3;
    public HotWaterHeater() {}



    @Override
    public void rotate(Boolean direction) {
        if (direction){
            EngineDirection += 1;
        }
        else {
            EngineDirection -= 1;
        }
        EngineDirection = EngineDirection % 4;
    }



    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y){
        return EngineDirection == 3;
    }




    @Override
    public void accept(ComponentAction visitor, PlayerState state) {
        visitor.visit(this, state);
    }


    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        if (type == 1) {
            playerBoard.setEnginePower(1);
        }

        playerBoard.getHotWaterHeaters().add(this);
        tile.sendUpdates(null,0, false, false, 0);
    }




    @Override
    public void remove(PlayerBoard playerBoard)  {
        if (type == 1) {
            playerBoard.setEnginePower(-1);
        }

        playerBoard.getHotWaterHeaters().remove(this);
        tile.sendUpdates(new RemoveTileEvent());
    }


    public int getEnginePower(){
        if (type == 2){
            return 2;
        }
        else {
            return 0;
        }
    }

    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        HotWaterHeater clone = new HotWaterHeater();
        clone.type = this.type;
        return clone;
    }


}

