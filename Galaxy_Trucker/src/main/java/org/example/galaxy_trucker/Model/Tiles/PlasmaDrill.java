package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.CANNON;
import org.example.galaxy_trucker.Model.IntegerPair;


import java.util.ArrayList;
import java.util.HashMap;

public class PlasmaDrill extends Component{


    private int CannonDirection = 1;

    @Override
    public void rotate(Boolean direction) {
        if (direction) {
            CannonDirection = (CannonDirection + 1) % 4;
        }
        else{
            CannonDirection = (CannonDirection - 1) % 4;
        }
    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y){
        int[][] mat = pb.getValidPlayerBoard();
        if (CannonDirection == 0 && y-1 >= 0 && mat[x][y-1]==1) return false;
        if (CannonDirection == 1 && x-1 >= 0 && mat[x-1][y]==1) return false;
        if (CannonDirection == 2 && y+1 < 10 && mat[x][y+1]==1) return false;
        if (CannonDirection == 3 && x+1 < 10 && mat[x+1][y]==1) return false;
        return true;
    };


    @Override
    public void insert(PlayerBoard playerBoard) {
        if (type == 2) {
            if (CannonDirection == 1){
                playerBoard.setPlasmaDrillsPower(2);
            }
            else {
                playerBoard.setPlasmaDrillsPower(1);
            }
        }
        else{
            if (CannonDirection == 1){
                playerBoard.setPlasmaDrillsPower(1);
            }
            else {
                playerBoard.setPlasmaDrillsPower(0.5);
            }
        }
        playerBoard.getPlasmaDrills().add(this);
    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        if (type == 2) {
            if (CannonDirection == 1){
                playerBoard.setPlasmaDrillsPower(-2);
            }
            else {
                playerBoard.setPlasmaDrillsPower(-1);
            }
        }
        else{
            if (CannonDirection == 1){
                playerBoard.setPlasmaDrillsPower(-1);
            }
            else {
                playerBoard.setPlasmaDrillsPower(-0.5);
            }
        }
        playerBoard.getPlasmaDrills().remove(this);
    }



    @Override
    public void setType(int type){
    }

}
