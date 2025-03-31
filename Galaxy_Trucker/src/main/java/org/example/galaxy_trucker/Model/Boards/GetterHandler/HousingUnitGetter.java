package org.example.galaxy_trucker.Model.Boards.GetterHandler;

import org.example.galaxy_trucker.Exceptions.HousingUnitEmptyException;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.*;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.HousingAlienGetter;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.HousingHumanGetter;
import org.example.galaxy_trucker.Model.Tiles.ComponentSetters.HousingSetter;

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

        if (!checkExistence(playerBoard.getClassifiedTiles(),coordinate, ModularHousingUnit.class, MainCockpitComp.class)){
            throw new InvalidInput("The following tile is not a modularHousingUnit");
        }

//        if (coordinate.getFirst() < 0 || coordinate.getFirst() >= pb.length || coordinate.getSecond() < 0 || coordinate.getSecond() >= pb[0].length || ValidPlayerBoard[coordinate.getFirst()][coordinate.getSecond()] == -1) {
//            throw new InvalidInput(coordinate.getFirst(), coordinate.getSecond(), "Invalid input: coordinates out of bounds or invalid tile." );
//        }
        HousingUnit unit =  ((HousingUnit) pb[coordinate.getFirst()][coordinate.getSecond()].getComponent());
//        if ((purpleAlien && !unit.isPurpleAlien()) || (brownAlien && !unit.isBrownAlien())) {
//            throw new HousingUnitEmptyException("There is no alien to kill");
//        }

        if (humans > ((int) unit.get(new HousingHumanGetter(unit))) && (!purpleAlien && !((boolean) unit.get(new HousingAlienGetter(unit, true)))) && (!brownAlien && !((boolean) unit.get(new HousingAlienGetter(unit, false))))){
            throw new HousingUnitEmptyException("It is not possible to kill in this Tile");
        }

        if (!purpleAlien && !unit.isPurpleAlien()){
            playerBoard.setPurpleAlien(false);
        }

        if (!brownAlien && unit.isBrownAlien()){
            playerBoard.setBrownAlien(false);
        }

        unit.set(new HousingSetter(unit, humans, purpleAlien, brownAlien));
        return true;
    }

    public boolean checkExistence(Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles, IntegerPair tiles, Class<?> type1, Class<?> type2){

        return  (classifiedTiles.containsKey(type1) &&
                classifiedTiles.get(type1).contains(tiles) ) ||
                (classifiedTiles.containsKey(type2) &&
                        classifiedTiles.get(type2).contains(tiles))
                ;

    }

}
