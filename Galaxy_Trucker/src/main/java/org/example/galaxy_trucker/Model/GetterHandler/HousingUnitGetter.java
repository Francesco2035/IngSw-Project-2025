package org.example.galaxy_trucker.Model.GetterHandler;

import org.example.galaxy_trucker.Exceptions.HousingUnitEmptyException;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.modularHousingUnit;

import java.util.ArrayList;
import java.util.Map;

public class HousingUnitGetter  implements PlayerBoardGetters{


    private PlayerBoard playerBoard;
    private IntegerPair coordinate;
    private int humans;
    private boolean purpleAlien;
    private boolean brownAlien;

    public HousingUnitGetter(PlayerBoard playerBoard, IntegerPair coordinate, int humans , boolean purpleAlien, boolean brownAlien){
        this.playerBoard = playerBoard;
        this.coordinate = coordinate;
        this.humans = humans;
        this.purpleAlien = purpleAlien;
        this.brownAlien = brownAlien;
    }

    /**
     * Method kill reduces the number of Human or Alien in a housing cell by 1 given the coordinate of this cell
     *
     * @throws ArrayIndexOutOfBoundsException when the user input is not in the correct range.
     * @throws HousingUnitEmptyException when the housingUnit is empty or is not possible to kill such a number of humans.
     */
    @Override
    public Object get(){

        Tile[][] pb = playerBoard.getPlayerBoard();
        int[][] ValidPlayerBoard = playerBoard.getValidPlayerBoard();
        int x = coordinate.getFirst();
        int y = coordinate.getSecond();

        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }

        if (!checkExistence(playerBoard.getClassifiedTiles(),coordinate, modularHousingUnit.class)){
            throw new InvalidInput("The following tile is not a modularHousingUnit");
        }

        if (coordinate.getFirst() < 0 || coordinate.getFirst() >= pb.length || coordinate.getSecond() < 0 || coordinate.getSecond() >= pb[0].length || ValidPlayerBoard[coordinate.getFirst()][coordinate.getSecond()] == -1) {
            throw new InvalidInput(coordinate.getFirst(), coordinate.getSecond(), "Invalid input: coordinates out of bounds or invalid tile." );
        }
        Component unit =  pb[coordinate.getFirst()][coordinate.getSecond()].getComponent();
        if ((purpleAlien && !unit.isPurpleAlien()) || (brownAlien && !unit.isBrownAlien())) {
            throw new HousingUnitEmptyException("There is no alien to kill");
        }

        if (humans > unit.getAbility()){
            throw new HousingUnitEmptyException("It is not possible to kill such a number of humans");
        }
        unit.setAbility(humans, purpleAlien, brownAlien);
        return null;
    }


    public boolean checkExistence(Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles, IntegerPair tile, Class<?> type){
        return  classifiedTiles.containsKey(type) &&
                classifiedTiles.get(type).contains(tile);
    }



}
