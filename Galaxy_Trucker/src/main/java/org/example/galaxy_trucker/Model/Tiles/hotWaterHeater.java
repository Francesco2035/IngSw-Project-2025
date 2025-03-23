package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import java.util.ArrayList;

public class hotWaterHeater extends Component{

    private boolean isDouble;

    public hotWaterHeater() {
    }





    public boolean isDouble() {
        return isDouble;
    }

    public void setDouble(boolean aDouble) {
        isDouble = aDouble;
    }



    @Override
    public int getAbility() {
        if (isDouble) {return 2;}
        else {return 1;}
    }

    @Override
    public ArrayList<Goods> getAbility(Goods good) {
        return null;
    }

    @Override
    public ArrayList<Integer> getAbility(int integer) {
        return null;
    }

    @Override
    public int setAbility() {
        return 0;
    }

    @Override
    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien) {
        return 0;
    }

    @Override
    public int setAbility(Goods good, boolean select) {
        return 0;
    }

    @Override
    public void setAbility(boolean direzione) {

    }


    @Override
    public void initType(){
        if (type.equals("single")) isDouble = false;
        else if (type.equals("double")) isDouble = true;
    }

    @Override
    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {

    }


    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y, Tile tile){
        int[][] mat = pb.getValidPlayerBoard();

        int index = tile.getConnectors().indexOf(Connector.MOTOR);


        if (index != 3 || mat[x + 1][y]==1 ){
            System.out.println(index + " " + x + " : " + y);
            return false;
        }
        return true;
        //return index == 3 && mat[x][y + 1] != 1;
    };

}

